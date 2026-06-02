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
import pt.ul.fc.css.tascaeats.common.dto.DeliveryPersonRegistertDto;
import pt.ul.fc.css.tascaeats.common.dto.DeliveryPersonResponseDto;
import pt.ul.fc.css.tascaeats.common.dto.KafkaResponse;
import pt.ul.fc.css.tascaeats.common.kafka.Topics;
import pt.ul.fc.css.tascaeats.delivery.dto.response.DeliveryResponseDto;
import pt.ul.fc.css.tascaeats.delivery.dto.request.AssignmentRequestDto;
import pt.ul.fc.css.tascaeats.delivery.exceptions.UserAlreadyExistsException;
import pt.ul.fc.css.tascaeats.delivery.service.DeliveryPersonService;
import pt.ul.fc.css.tascaeats.delivery.service.DeliveryService;

@Component
public class Listener {

  @Autowired private DeliveryPersonService deliveryPersonService;

  @Autowired private DeliveryService deliveryService;

  @Autowired private Sender sender;

  @RetryableTopic(attempts = "4", backoff = @Backoff(delay = 2000), dltTopicSuffix = "-dlt")
  @KafkaListener(topics = Topics.DELIVERY_PERSON_REGISTER)
  public void listenRegisterDeliveryPerson(String message) throws Exception {

    DeliveryPersonRegistertDto dto = Sender.fromJson(message, new TypeReference<>() {});

    System.out.println(
        "Received delivery person event with correlationId: " + dto.getCorrelationId());

    try {
      DeliveryPersonResponseDto result = deliveryPersonService.registerNewDeliveryPerson(dto);

      if (result == null) {
        // evento já processado, não envia resposta
        return;
      }

      System.out.println(
          "Successfully registered delivery person, sending response for correlationId: "
              + dto.getCorrelationId());
      sender.sendAsJson(Topics.DELIVERY_PERSON_REGISTER_RESPONSE, KafkaResponse.ok(result));
    } catch (UserAlreadyExistsException e) {
      // erro de negócio — não faz sentido retry, resolve logo
      sender.sendAsJson(
          Topics.DELIVERY_PERSON_REGISTER_RESPONSE,
          KafkaResponse.error("USER_ALREADY_EXISTS", e.getMessage()));
    } catch (Exception e) {
      // erro técnico — deixa o @RetryableTopic tentar novamente
      throw e;
    }
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
      sender.sendAsJson(Topics.DELIVERY_MARKED_DELIVERED_RESPONSE, response);
    } catch (Exception e) {
      System.err.println("Error processing mark-delivery-delivered: " + e.getMessage());
    }
  }

  @KafkaListener(topics = Topics.ASSIGN_DELIVERY_PERSON, groupId = "tascaeats-group")
  public void listenAssignDeliveryPerson(String message) {
    try {
      AssignmentRequestDto dto = Sender.fromJson(message, new TypeReference<AssignmentRequestDto>() {});
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

  @DltHandler
  public void handleDlt(String requestMsg, @Header(KafkaHeaders.EXCEPTION_MESSAGE) String error) {
    System.out.println("Message sent to DLT after retries. Payload: " + requestMsg);
    System.out.println("Dead because: " + error);
  }
}
