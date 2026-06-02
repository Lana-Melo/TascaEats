package pt.ul.fc.css.tascaeats.delivery.service;

import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.stereotype.Service;
import pt.ul.fc.css.tascaeats.common.dto.DeleteDeliveryPersonDto;
import pt.ul.fc.css.tascaeats.common.dto.DeliveryPersonResponseDto;
import pt.ul.fc.css.tascaeats.common.dto.KafkaResponse;
import pt.ul.fc.css.tascaeats.common.dto.RegisterDeliveryPersonDto;
import pt.ul.fc.css.tascaeats.common.dto.UpdateDeliveryPersonDto;
import pt.ul.fc.css.tascaeats.common.kafka.Topics;
import pt.ul.fc.css.tascaeats.delivery.exceptions.UserAlreadyExistsException;
import pt.ul.fc.css.tascaeats.delivery.exceptions.UserNotFoundException;
import pt.ul.fc.css.tascaeats.delivery.kafka.KafkaResponseStore;
import pt.ul.fc.css.tascaeats.delivery.kafka.Sender;
import pt.ul.fc.css.tascaeats.delivery.model.DeliveryPerson;
import pt.ul.fc.css.tascaeats.delivery.model.DeliveryPersonStatus;
import pt.ul.fc.css.tascaeats.delivery.model.ProcessedEvent;
import pt.ul.fc.css.tascaeats.delivery.repository.DeliveryPersonRepository;
import pt.ul.fc.css.tascaeats.delivery.repository.ProcessedEventRepository;
import pt.ul.fc.css.tascaeats.delivery.util.Mapper;

@Service
public class DeliveryPersonService {

  private final DeliveryPersonRepository deliveryPersonRepository;
  private final ProcessedEventRepository processedEventRepository;
  private final Sender sender;
  private final KafkaResponseStore kafkaResponseStore;

  public DeliveryPersonService(
      DeliveryPersonRepository deliveryPersonRepository,
      ProcessedEventRepository processedEventRepository,
      Sender sender,
      KafkaResponseStore kafkaResponseStore) {
    this.deliveryPersonRepository = deliveryPersonRepository;
    this.processedEventRepository = processedEventRepository;
    this.sender = sender;
    this.kafkaResponseStore = kafkaResponseStore;
  }

  @Transactional
  public DeliveryPersonResponseDto registerNewDeliveryPerson(RegisterDeliveryPersonDto request)
      throws Exception {
    if (request.getCorrelationId() != null) {
      if (processedEventRepository.existsById(request.getCorrelationId())) {
        System.out.println("Event " + request.getCorrelationId() + " already processed. Ignoring.");
        return null;
      }
      System.out.println("Saving event with correlationId: " + request.getCorrelationId());
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
  }

  @Transactional
  public DeliveryPersonResponseDto updateDeliveryPerson(UpdateDeliveryPersonDto request) {
    if (request.getCorrelationId() != null) {
      if (processedEventRepository.existsById(request.getCorrelationId())) {
        System.out.println("Event " + request.getCorrelationId() + " already processed. Ignoring.");
        return null;
      }
      System.out.println("Saving event with correlationId: " + request.getCorrelationId());
      processedEventRepository.save(new ProcessedEvent(request.getCorrelationId()));
    }

    Optional<DeliveryPerson> userOpt =
        deliveryPersonRepository.findByUsername(request.getUsername());
    if (userOpt.isEmpty()) {
      throw new UserNotFoundException("User not found");
    }

    DeliveryPerson deliveryPerson = userOpt.get();

    if (request.getName() != null && !request.getName().isEmpty()) {
      deliveryPerson.setName(request.getName());
    }
    if (request.getPhoneNumber() != null && !request.getPhoneNumber().isEmpty()) {
      deliveryPerson.setPhoneNumber(request.getPhoneNumber());
    }
    if (request.getNif() != null && !request.getNif().isEmpty()) {
      deliveryPerson.setNif(request.getNif());
    }
    if (request.getPassword() != null && !request.getPassword().isEmpty()) {
      deliveryPerson.setPassword(request.getPassword());
    }
    if (request.getStatus() != null && !request.getStatus().isEmpty()) {
      deliveryPerson.setDeliveryPersonStatus(DeliveryPersonStatus.valueOf(request.getStatus()));
    }

    DeliveryPerson saved = deliveryPersonRepository.save(deliveryPerson);
    return Mapper.mapDeliveryPersonToDeliveryPersonResponse(saved);
  }

  // retorna o mode ("HARD"/"SOFT") se aprovado, null se negado pelo monolito
  public String deleteDeliveryPerson(DeleteDeliveryPersonDto request) throws Exception {
    sender.sendAsJson(Topics.DELIVERY_PERSON_DELETE_REQUESTED, request);

    KafkaResponse<String> decision = kafkaResponseStore.waitForDeleteDecision(10000);

    if (decision == null) {
      throw new IllegalStateException("Timeout waiting for delete decision from monolith");
    }

    if (!decision.isSuccess()) {
      // DENIED — delete não é permitido
      return null;
    }

    String mode = decision.getData(); // "HARD" ou "SOFT"
    DeliveryPerson deliveryPerson =
        deliveryPersonRepository
            .findByUsername(request.getUsername())
            .orElseThrow(() -> new UserNotFoundException("User not found"));

    if ("HARD".equals(mode)) {
      deliveryPersonRepository.deleteById(deliveryPerson.getId());
    } else {
      deliveryPerson.setDeletedAt(LocalDateTime.now());
      deliveryPersonRepository.save(deliveryPerson);
    }

    return mode;
  }
}
