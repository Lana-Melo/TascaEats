package pt.ul.fc.css.tascaeats.delivery.kafka;

import com.fasterxml.jackson.core.type.TypeReference;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Component;
import pt.ul.fc.css.tascaeats.common.dto.AssignmentRequestDto;
import pt.ul.fc.css.tascaeats.common.dto.DeleteDeliveryPersonDto;
import pt.ul.fc.css.tascaeats.common.dto.DeliveryPersonDeleteDecisionDto;
import pt.ul.fc.css.tascaeats.common.dto.DeliveryPersonResponseDto;
import pt.ul.fc.css.tascaeats.common.dto.KafkaResponse;
import pt.ul.fc.css.tascaeats.common.dto.RegisterDeliveryPersonDto;
import pt.ul.fc.css.tascaeats.common.dto.SearchDeliveryPersonDto;
import pt.ul.fc.css.tascaeats.common.dto.UpdateDeliveryPersonDto;
import pt.ul.fc.css.tascaeats.common.kafka.Topics;
import pt.ul.fc.css.tascaeats.delivery.dto.response.DeliveryResponseDto;
import pt.ul.fc.css.tascaeats.delivery.exceptions.UserAlreadyExistsException;
import pt.ul.fc.css.tascaeats.delivery.exceptions.UserNotFoundException;
import pt.ul.fc.css.tascaeats.delivery.service.DeliveryPersonService;
import pt.ul.fc.css.tascaeats.delivery.service.DeliveryService;

@Component
public class Listener {

  @Autowired private DeliveryPersonService deliveryPersonService;
  @Autowired private DeliveryService deliveryService;
  @Autowired private Sender sender;
  @Autowired private KafkaResponseStore kafkaResponseStore;

  @RetryableTopic(attempts = "4", backoff = @Backoff(delay = 2000), dltTopicSuffix = "-dlt")
  @KafkaListener(topics = Topics.DELIVERY_PERSON_REGISTER)
  public void listenRegisterDeliveryPerson(String message) throws Exception {
    RegisterDeliveryPersonDto dto = Sender.fromJson(message, new TypeReference<>() {});
    System.out.println(
        "Received delivery person event with correlationId: " + dto.getCorrelationId());

    try {
      DeliveryPersonResponseDto result = deliveryPersonService.registerNewDeliveryPerson(dto);
      if (result == null) {
        return;
      }
      System.out.println(
          "Successfully registered delivery person, sending response for correlationId: "
              + dto.getCorrelationId());
      sender.sendAsJson(Topics.DELIVERY_PERSON_REGISTER_RESPONSE, KafkaResponse.ok(result));
    } catch (UserAlreadyExistsException e) {
      sender.sendAsJson(
          Topics.DELIVERY_PERSON_REGISTER_RESPONSE,
          KafkaResponse.error("USER_ALREADY_EXISTS", e.getMessage()));
    } catch (Exception e) {
      throw e;
    }
  }

  @RetryableTopic(attempts = "4", backoff = @Backoff(delay = 2000), dltTopicSuffix = "-dlt")
  @KafkaListener(topics = Topics.DELIVERY_PERSON_UPDATE)
  public void listenUpdateDeliveryPerson(String message) throws Exception {
    UpdateDeliveryPersonDto dto = Sender.fromJson(message, new TypeReference<>() {});
    System.out.println(
        "Received delivery person update event with correlationId: " + dto.getCorrelationId());

    try {
      DeliveryPersonResponseDto result = deliveryPersonService.updateDeliveryPerson(dto);
      if (result == null) {
        return;
      }
      System.out.println(
          "Successfully update delivery person, sending response for correlationId: "
              + dto.getCorrelationId());
      sender.sendAsJson(Topics.DELIVERY_PERSON_UPDATE_RESPONSE, KafkaResponse.ok(result));
    } catch (UserNotFoundException e) {
      sender.sendAsJson(
          Topics.DELIVERY_PERSON_UPDATE_RESPONSE,
          KafkaResponse.error("USER_NOT_FOUND", e.getMessage()));
    } catch (Exception e) {
      throw e;
    }
  }

  @RetryableTopic(attempts = "4", backoff = @Backoff(delay = 2000), dltTopicSuffix = "-dlt")
  @KafkaListener(topics = Topics.DELIVERY_PERSON_DELETE)
  public void listenDeleteDeliveryPerson(String message) throws Exception {
    DeleteDeliveryPersonDto dto = Sender.fromJson(message, new TypeReference<>() {});
    System.out.println(
        "Received delivery person delete event with correlationId: " + dto.getCorrelationId());

    try {
      String mode = deliveryPersonService.deleteDeliveryPerson(dto);
      KafkaResponse<String> response =
          mode != null
              ? KafkaResponse.ok(mode)
              : KafkaResponse.error("DENIED", "Delete denied by monolith");
      System.out.println(
          "Delete processed (mode="
              + mode
              + "), sending response for correlationId: "
              + dto.getCorrelationId());
      sender.sendAsJson(Topics.DELIVERY_PERSON_DELETE_RESPONSE, response);
    } catch (Exception e) {
      sender.sendAsJson(
          Topics.DELIVERY_PERSON_DELETE_RESPONSE,
          KafkaResponse.error("INTERNAL_ERROR", e.getMessage()));
      throw e;
    }
  }

  @RetryableTopic(attempts = "4", backoff = @Backoff(delay = 2000), dltTopicSuffix = "-dlt")
  @KafkaListener(topics = Topics.DELIVERY_PERSON_DELETED_APPROVED)
  public void listenDeleteApproved(String message) throws Exception {
    DeliveryPersonDeleteDecisionDto dto = Sender.fromJson(message, new TypeReference<>() {});
    System.out.println(
        "Received delete approved (mode="
            + dto.getMode()
            + ") for correlationId: "
            + dto.getCorrelationId());
    kafkaResponseStore.putDeleteDecision(KafkaResponse.ok(dto.getMode()));
  }

  @RetryableTopic(attempts = "4", backoff = @Backoff(delay = 2000), dltTopicSuffix = "-dlt")
  @KafkaListener(topics = Topics.DELIVERY_PERSON_DELETED_DENIED)
  public void listenDeleteDenied(String message) throws Exception {
    DeleteDeliveryPersonDto dto = Sender.fromJson(message, new TypeReference<>() {});
    System.out.println("Received delete denied for correlationId: " + dto.getCorrelationId());
    kafkaResponseStore.putDeleteDecision(
        KafkaResponse.error("DENIED", "Delete denied by monolith"));
  }

  @RetryableTopic(attempts = "4", backoff = @Backoff(delay = 2000), dltTopicSuffix = "-dlt")
  @KafkaListener(topics = Topics.DELIVERY_PERSON_SEARCH_BY)
  public void listenSearchDeliveryPersons(String message) {
    SearchDeliveryPersonDto dto = Sender.fromJson(message, new TypeReference<>() {});
    System.out.println("Received delivery person search event");
    List<DeliveryPersonResponseDto> results = deliveryPersonService.searchDeliveryPersons(dto);
    sender.sendAsJson(Topics.DELIVERY_PERSON_SEARCH_BY_RESPONSE, KafkaResponse.ok(results));
  }

  @KafkaListener(topics = Topics.GET_MY_DELIVERIES, groupId = "tascaeats-group")
  public void listenGetMyDeliveries(String message) {
    Long deliveryPersonId = Sender.fromJson(message, new TypeReference<Long>() {});
    System.out.println("Received get-my-deliveries for deliveryPersonId: " + deliveryPersonId);
    List<DeliveryResponseDto> deliveries =
        deliveryService.getDeliveriesByDeliveryPerson(deliveryPersonId);
    sender.sendAsJson(Topics.MY_DELIVERIES_RESPONSE, deliveries);
  }

  @KafkaListener(topics = Topics.MARK_DELIVERY_DELIVERED, groupId = "tascaeats-group")
  public void listenMarkDelivered(String message) {
    try {
      Long deliveryId = Sender.fromJson(message, new TypeReference<Long>() {});
      System.out.println("Received mark-delivery-delivered for deliveryId: " + deliveryId);
      DeliveryResponseDto response = deliveryService.updateDeliveryStatus(deliveryId);
      // publica a resposta para o monolito desbloquear o waitForDelivery
      sender.sendAsJson(Topics.DELIVERY_MARKED_DELIVERED_RESPONSE, KafkaResponse.ok(response));
    } catch (Exception e) {
      System.err.println("Error processing mark-delivery-delivered: " + e.getMessage());
    }
  }

  @KafkaListener(topics = Topics.ASSIGN_DELIVERY_PERSON, groupId = "tascaeats-group")
  public void listenAssignDeliveryPerson(String message) {
    try {
      AssignmentRequestDto dto =
          Sender.fromJson(message, new TypeReference<AssignmentRequestDto>() {});
      System.out.println(
          "Received assign-delivery-person for deliveryId: "
              + dto.getDeliveryId()
              + ", deliveryPersonId: "
              + dto.getDeliveryPersonId());
      DeliveryResponseDto response =
          deliveryService.assignDeliveryPerson(dto.getDeliveryId(), dto.getDeliveryPersonId());
      // publica a resposta para o monolito desbloquear o waitForDelivery
      sender.sendAsJson(Topics.DELIVERY_PERSON_ASSIGNED, response);
    } catch (Exception e) {
      System.err.println("Error processing assign-delivery-person: " + e.getMessage());
    }
  }

  @KafkaListener(topics = Topics.CREATE_DELIVERY, groupId = "tascaeats-group")
  public void listenCreateDelivery(String message) {
    try {
      Long orderId = Sender.fromJson(message, new TypeReference<Long>() {});
      System.out.println("Received create-delivery for orderId: " + orderId);
      DeliveryResponseDto response = deliveryService.createDelivery(orderId);
      sender.sendAsJson(Topics.CREATE_DELIVERY_RESPONSE, KafkaResponse.ok(response));
    } catch (Exception e) {
      System.err.println("Error processing create-delivery: " + e.getMessage());
    }
  }

  @DltHandler
  public void handleDlt(String requestMsg, @Header(KafkaHeaders.EXCEPTION_MESSAGE) String error) {
    System.out.println("Message sent to DLT after retries. Payload: " + requestMsg);
    System.out.println("Dead because: " + error);
  }
}
