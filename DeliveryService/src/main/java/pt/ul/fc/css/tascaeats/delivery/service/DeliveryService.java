package pt.ul.fc.css.tascaeats.delivery.service;

import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import pt.ul.fc.css.tascaeats.delivery.dto.request.DeliveryPersonRequestDto;
import pt.ul.fc.css.tascaeats.delivery.dto.response.DeliveryPersonResponseDto;
import pt.ul.fc.css.tascaeats.delivery.dto.response.DeliveryResponseDto;
import pt.ul.fc.css.tascaeats.delivery.exceptions.RequirementsNotMetException;
import pt.ul.fc.css.tascaeats.delivery.kafka.Sender;
import pt.ul.fc.css.tascaeats.delivery.model.Delivery;
import pt.ul.fc.css.tascaeats.delivery.model.DeliveryPerson;
import pt.ul.fc.css.tascaeats.delivery.model.DeliveryPersonStatus;
import pt.ul.fc.css.tascaeats.delivery.model.DeliveryStatus;
import pt.ul.fc.css.tascaeats.delivery.repository.DeliveryPersonRepository;
import pt.ul.fc.css.tascaeats.delivery.repository.DeliveryRepository;

@Service
public class DeliveryService {

  @Autowired private DeliveryRepository deliveryRepository;

  @Autowired private DeliveryPersonRepository deliveryPersonRepository;

  @Autowired private Sender sender;

  public DeliveryPersonResponseDto registerNewDeliveryPerson(DeliveryPersonRequestDto request) {
    if (deliveryPersonRepository.findByUsername(request.getUsername()).isPresent()) {
      throw new IllegalArgumentException("Username already exists.");
    }
    DeliveryPerson deliveryPerson =
        new DeliveryPerson(
            request.getUsername(),
            request.getPassword(),
            request.getName(),
            request.getPhoneNumber(),
            request.getNif());
    DeliveryPerson saved = deliveryPersonRepository.save(deliveryPerson);
    return mapDeliveryToDeliveryPersonResponse(saved);
  }

  public DeliveryResponseDto createDelivery(Long orderId) {
    Delivery delivery = new Delivery(LocalDateTime.now(), orderId);
    Delivery saved = deliveryRepository.save(delivery);
    return mapDeliveryToDeliveryResponse(saved);
  }

  public List<DeliveryResponseDto> getAllDeliveries() {
    return deliveryRepository.findAll().stream().map(this::mapDeliveryToDeliveryResponse).toList();
  }

  private Specification<DeliveryPerson> getSearchSpec(
      String name, String userType, String delStatus, String deliveryNumber) {
    return (root, query, cb) -> {
      List<Predicate> predicates = new ArrayList<>();

      if (name != null && !name.isEmpty()) {
        predicates.add(cb.like(cb.upper(root.get("name")), "%" + name.toUpperCase() + "%"));
      }

      if (delStatus != null && !delStatus.isEmpty()) {
        DeliveryPersonStatus status = DeliveryPersonStatus.valueOf(delStatus);
        predicates.add(cb.equal(root.get("deliveryPersonStatus"), status));
      }

      if (deliveryNumber != null && !deliveryNumber.isEmpty()) {
        Subquery<Long> subquery = query.subquery(Long.class);
        Root<Delivery> orderRoot = subquery.from(Delivery.class);
        subquery
            .select(cb.count(orderRoot))
            .where(
                cb.and(
                    cb.equal(orderRoot.get("deliveryPerson"), root),
                    cb.equal(orderRoot.get("status"), DeliveryStatus.DELIVERED)));
        predicates.add(cb.greaterThanOrEqualTo(subquery, Long.parseLong(deliveryNumber)));
      }

      return cb.and(predicates.toArray(new Predicate[0]));
    };
  }

  @Transactional
  public DeliveryResponseDto assignDeliveryPerson(Long id, Long deliveryPersonId) {
    Optional<Delivery> deliveryOpt = deliveryRepository.findById(id);
    if (deliveryOpt.isEmpty()) {
      throw new ResourceNotFoundException("Delivery not found");
    }
    Optional<DeliveryPerson> deliveryPersonOpt =
        deliveryPersonRepository.findById(deliveryPersonId);
    if (deliveryPersonOpt.isEmpty()) {
      throw new ResourceNotFoundException("Delivery person not found");
    }

    DeliveryPerson del = (DeliveryPerson) deliveryPersonOpt.get();
    Delivery delivery = deliveryOpt.get();
    if (del.getDeliveryPersonStatus() == DeliveryPersonStatus.BUSY) {
      throw new RequirementsNotMetException("Delivery person must be available");
    }
    delivery.setDeliveryPerson(del);
    del.setDeliveryPersonStatus(DeliveryPersonStatus.BUSY);

    deliveryPersonRepository.save(del);
    Delivery saved = deliveryRepository.save(delivery);
    sender.sendAsJson(
        "delivery-person-assigned",
        Map.of(
            "orderId", saved.getOrderId(),
            "deliveryPersonId", del.getId(),
            "deliveryPersonName", del.getName()));
    return mapDeliveryToDeliveryResponse(saved);
  }

  public List<DeliveryResponseDto> getDeliveriesByDeliveryPerson(Long deliveryPersonId) {
    return deliveryRepository.findByDeliveryPerson_Id(deliveryPersonId).stream()
        .map(this::mapDeliveryToDeliveryResponse)
        .toList();
  }

  @Transactional
  public DeliveryResponseDto updateDeliveryStatus(Long id) {
    Optional<Delivery> ordOpt = deliveryRepository.findById(id);
    if (ordOpt.isEmpty()) {
      throw new ResourceNotFoundException("Delivery not found");
    }
    Delivery delivery = ordOpt.get();

    if (delivery.getStatus() != DeliveryStatus.INCOMING) {
      throw new RequirementsNotMetException("Delivery has already been delivered");
    }
    delivery.setDeliveryTimeStamp();
    DeliveryPerson del = delivery.getDeliveryPerson();
    del.setDeliveryPersonStatus(DeliveryPersonStatus.AVAILABLE);
    deliveryPersonRepository.save(del);

    delivery.setStatus(DeliveryStatus.DELIVERED);
    Delivery saved = deliveryRepository.save(delivery);
    sender.sendAsJson(
        "delivery-delivered", Map.of("orderId", saved.getOrderId())); // vai monolito tua vez
    return mapDeliveryToDeliveryResponse(saved);
  }

  private DeliveryResponseDto mapDeliveryToDeliveryResponse(Delivery delivery) {
    Long delId = delivery.getDeliveryPerson() == null ? null : delivery.getDeliveryPerson().getId();
    String delName =
        delivery.getDeliveryPerson() == null ? null : delivery.getDeliveryPerson().getName();
    String delNif =
        delivery.getDeliveryPerson() == null ? null : delivery.getDeliveryPerson().getNif();

    return new DeliveryResponseDto(
        delivery.getId(),
        delivery.getStatus(),
        delivery.getOrderId(),
        delivery.getPickedUpTimeStamp(),
        delivery.getDeliveryTimeStamp(),
        delId,
        delName,
        delNif);
  }

  private DeliveryPersonResponseDto mapDeliveryToDeliveryPersonResponse(
      DeliveryPerson deliveryPerson) {
    return new DeliveryPersonResponseDto(
        deliveryPerson.getId(),
        deliveryPerson.getUsername(),
        deliveryPerson.getName(),
        deliveryPerson.getPhoneNumber(),
        deliveryPerson.getNif(),
        deliveryPerson.getDeliveryPersonStatus().toString());
  }
}
