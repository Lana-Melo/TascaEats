package pt.ul.fc.css.tascaeats.delivery.service;

import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.ul.fc.css.tascaeats.delivery.dto.response.DeliveryResponseDto;
import pt.ul.fc.css.tascaeats.delivery.exceptions.RequirementsNotMetException;
import pt.ul.fc.css.tascaeats.delivery.kafka.Sender;
import pt.ul.fc.css.tascaeats.common.kafka.Topics;
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

  public DeliveryResponseDto createDelivery(Long orderId) {
    Delivery delivery = new Delivery(LocalDateTime.now(), orderId);
    Delivery saved = deliveryRepository.save(delivery);

    Optional<DeliveryPerson> availableOpt = deliveryPersonRepository.findFirstByStatus(DeliveryPersonStatus.AVAILABLE);
    if (availableOpt.isPresent()) {
      DeliveryPerson dp = availableOpt.get();
      DeliveryResponseDto assigned = assignDeliveryPerson(saved.getId(), dp.getId());
      sender.sendAsJson(Topics.CREATE_DELIVERY_RESPONSE, assigned);
      return assigned;
    }

    sender.sendAsJson(
        Topics.CREATE_DELIVERY_RESPONSE,
        Map.of(
            "orderId", saved.getOrderId(),
            "deliveryId", saved.getId(),
            "status", saved.getStatus().toString()));
    return mapDeliveryToDeliveryResponse(saved);
  }

  public List<DeliveryResponseDto> getAllDeliveries() {
    return deliveryRepository.findAll().stream().map(this::mapDeliveryToDeliveryResponse).toList();
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
    delivery.setStatus(DeliveryStatus.INCOMING);
    del.setDeliveryPersonStatus(DeliveryPersonStatus.BUSY);

    deliveryPersonRepository.save(del);
    Delivery saved = deliveryRepository.save(delivery);
    sender.sendAsJson(
        Topics.DELIVERY_PERSON_ASSIGNED,
        Map.of(
            "orderId", saved.getOrderId(),
            "deliveryPersonId", del.getId(),
            "deliveryPersonUsername", del.getUsername()));
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
        Topics.DELIVERY_DELIVERED, Map.of("orderId", saved.getOrderId())); // vai monolito tua vez
    return mapDeliveryToDeliveryResponse(saved);
  }

  private DeliveryResponseDto mapDeliveryToDeliveryResponse(Delivery delivery) {
    Long delId = delivery.getDeliveryPerson() == null ? null : delivery.getDeliveryPerson().getId();
    String delUsername =
        delivery.getDeliveryPerson() == null ? null : delivery.getDeliveryPerson().getUsername();
    String delNif =
        delivery.getDeliveryPerson() == null ? null : delivery.getDeliveryPerson().getNif();

    return new DeliveryResponseDto(
        delivery.getId(),
        delivery.getStatus(),
        delivery.getOrderId(),
        delivery.getPickedUpTimeStamp(),
        delivery.getDeliveryTimeStamp(),
        delId,
        delUsername,
        delNif);
  }
}
