package pt.ul.fc.css.tascaeats.delivery.service;

import org.springframework.stereotype.Service;
import pt.ul.fc.css.tascaeats.common.dto.DeliveryPersonRegistertDto;
import pt.ul.fc.css.tascaeats.common.dto.DeliveryPersonResponseDto;
import pt.ul.fc.css.tascaeats.delivery.exceptions.UserAlreadyExistsException;
import pt.ul.fc.css.tascaeats.delivery.model.DeliveryPerson;
import pt.ul.fc.css.tascaeats.delivery.model.ProcessedEvent;
import pt.ul.fc.css.tascaeats.delivery.repository.DeliveryPersonRepository;
import pt.ul.fc.css.tascaeats.delivery.repository.ProcessedEventRepository;
import pt.ul.fc.css.tascaeats.delivery.util.Mapper;

@Service
public class DeliveryPersonService {

  private final DeliveryPersonRepository deliveryPersonRepository;
  private final ProcessedEventRepository processedEventRepository;

  public DeliveryPersonService(
      DeliveryPersonRepository deliveryPersonRepository,
      ProcessedEventRepository processedEventRepository) {
    this.deliveryPersonRepository = deliveryPersonRepository;
    this.processedEventRepository = processedEventRepository;
  }

  // NAO ESQUECER DE ADICIONAR STATUS
  public DeliveryPersonResponseDto registerNewDeliveryPerson(DeliveryPersonRegistertDto request)
      throws Exception {
    // garantir idempotencia
    if (request.getCorrelationId() != null) {
      if (processedEventRepository.existsById(request.getCorrelationId())) {
        System.out.println("Event " + request.getCorrelationId() + " already processed. Ignoring.");
        // devolve null ou um resultado vazio — o listener não envia resposta
        return null;
      }
      processedEventRepository.save(new ProcessedEvent(request.getCorrelationId()));
    }
    if (deliveryPersonRepository.findByUsername(request.getUsername()).isPresent()) {
      throw new UserAlreadyExistsException("Username already exists.");
    }
    DeliveryPerson deliveryPerson =
        new DeliveryPerson(
            request.getUsername(),
            request.getPassword(),
            request.getName(),
            request.getPhoneNumber(),
            request.getNif());
    DeliveryPerson saved = deliveryPersonRepository.save(deliveryPerson);

    return Mapper.mapDeliveryPersonToDeliveryPersonResponse(saved);
    // sender.sendAsJson("delivery-person-created", response);
  }
  /*
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
  }*/
}
