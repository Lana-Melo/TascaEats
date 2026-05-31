package pt.ul.fc.css.tascaeats.delivery.service;

import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import pt.ul.fc.css.tascaeats.delivery.dto.request.DeliveryPersonRequestDto;
import pt.ul.fc.css.tascaeats.delivery.dto.response.DeliveryPersonResponseDto;
import pt.ul.fc.css.tascaeats.delivery.kafka.Sender;
import pt.ul.fc.css.tascaeats.delivery.model.Delivery;
import pt.ul.fc.css.tascaeats.delivery.model.DeliveryPerson;
import pt.ul.fc.css.tascaeats.delivery.model.DeliveryPersonStatus;
import pt.ul.fc.css.tascaeats.delivery.model.DeliveryStatus;
import pt.ul.fc.css.tascaeats.delivery.repository.DeliveryPersonRepository;
import pt.ul.fc.css.tascaeats.delivery.repository.DeliveryRepository;
import pt.ul.fc.css.tascaeats.delivery.util.Mapper;

@Service
public class DeliveryPersonService {

  private final DeliveryPersonRepository deliveryPersonRepository;
  private final Sender sender;
  private final DeliveryRepository deliveryRepository;

  public DeliveryPersonService(
      DeliveryPersonRepository deliveryPersonRepository,
      Sender sender,
      DeliveryRepository deliveryRepository) {
    this.deliveryPersonRepository = deliveryPersonRepository;
    this.sender = sender;
    this.deliveryRepository = deliveryRepository;
  }

  public void registerNewDeliveryPerson(DeliveryPersonRequestDto request) {
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

    // trocar este return para emitir um evento com as infos necessarias para o monolito
    DeliveryPersonResponseDto response = Mapper.mapDeliveryPersonToDeliveryPersonResponse(saved);
    sender.sendAsJson("delivery-person-created", response);
  }

  public DeliveryPersonResponseDto getUserById(Long id) {
    Optional<DeliveryPerson> deliveryPersonOpt = deliveryPersonRepository.findById(id);
    if (deliveryPersonOpt.isEmpty()) {
      throw new ResourceNotFoundException("Delivery person not found");
    }
    return Mapper.mapDeliveryPersonToDeliveryPersonResponse(deliveryPersonOpt.get());
  }

  public List<DeliveryPersonResponseDto> searchBy(
      String name, String userType, String delStatus, String deliveryNumber) {
    Specification<DeliveryPerson> spec = getSearchSpec(name, userType, delStatus, deliveryNumber);
    List<DeliveryPerson> users = deliveryPersonRepository.findAll(spec);
    return users.stream().map(Mapper::mapDeliveryPersonToDeliveryPersonResponse).toList();
  }

  private Specification<DeliveryPerson> getSearchSpec(
      String name, String userType, String delStatus, String deliveryNumber) {
    return (root, query, cb) -> {
      List<Predicate> predicates = new ArrayList<>();
      if (name != null && !name.isEmpty()) {
        predicates.add(cb.like(cb.upper(root.get("name")), "%" + name.toUpperCase() + "%"));
      }
      if (delStatus != null && !delStatus.isEmpty()) {
        predicates.add(
            cb.equal(root.get("deliveryPersonStatus"), DeliveryPersonStatus.valueOf(delStatus)));
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
  public DeliveryPersonResponseDto updateDeliveryPerson(Long id, DeliveryPersonRequestDto request) {
    Optional<DeliveryPerson> userOpt = deliveryPersonRepository.findById(id);
    if (userOpt.isEmpty() || !(userOpt.get() instanceof DeliveryPerson)) {
      throw new ResourceNotFoundException("User not found");
    }

    DeliveryPerson deliveryPerson = (DeliveryPerson) userOpt.get();
    deliveryPerson.setName(request.getName());
    deliveryPerson.setUsername(request.getUsername());
    deliveryPerson.setPhoneNumber(request.getPhoneNumber());
    deliveryPerson.setNif(request.getNif());
    deliveryPerson.setPassword(request.getPassword());
    if (request.getStatus() != null && !request.getStatus().isEmpty()) {
      deliveryPerson.setDeliveryPersonStatus(DeliveryPersonStatus.valueOf(request.getStatus()));
    }

    DeliveryPerson saved = deliveryPersonRepository.save(deliveryPerson);
    return Mapper.mapDeliveryPersonToDeliveryPersonResponse(saved);
  }

  public boolean deleteDeliveryPerson(Long user) {
    DeliveryPerson deliveryPerson =
        deliveryPersonRepository
            .findById(user)
            .orElseThrow(() -> new ResourceNotFoundException("Delivery person not found"));
    if (!deliveryRepository.existsByDeliveryPersonId(deliveryPerson.getId())) {
      deliveryPersonRepository.deleteById(deliveryPerson.getId()); // definitively delete
    } else if (deliveryRepository.existsByDeliveryPersonIdAndStatusNotIn(
        deliveryPerson.getId(), List.of(DeliveryStatus.DELIVERED))) {
      return false;
    } else {
      deliveryPerson.setDeletedAt(LocalDateTime.now());
      deliveryPersonRepository.save(deliveryPerson);
    }
    return true;
  }
}
