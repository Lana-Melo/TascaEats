package pt.ul.fc.css.tascaeats.kafka;

import com.fasterxml.jackson.core.type.TypeReference;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Component;
import pt.ul.fc.css.tascaeats.common.dto.DeleteDeliveryPersonDto;
import pt.ul.fc.css.tascaeats.common.kafka.Topics;
import pt.ul.fc.css.tascaeats.services.OrderService;
import pt.ul.fc.css.tascaeats.services.UserService;

@Component
public class Listener {

  @Autowired private KafkaResponseStore responseStore;

  @Autowired private OrderService orderService;

  @Autowired private UserService userService;

  @RetryableTopic(attempts = "4", backoff = @Backoff(delay = 2000), dltTopicSuffix = "-dlt")
  @KafkaListener(topics = Topics.DELIVERY_PERSON_REGISTER_RESPONSE)
  public void listenDeliveryPersonCreated(String message) throws Exception {
    System.out.println("Received delivery person registration response");
    responseStore.putDeliveryPerson(message);
  }

  @RetryableTopic(attempts = "4", backoff = @Backoff(delay = 2000), dltTopicSuffix = "-dlt")
  @KafkaListener(topics = Topics.DELIVERY_PERSON_UPDATE_RESPONSE)
  public void listenDeliveryPersonUpdated(String message) throws Exception {
    System.out.println("Received delivery person update response");
    responseStore.putDeliveryPerson(message);
  }

  @RetryableTopic(attempts = "4", backoff = @Backoff(delay = 2000), dltTopicSuffix = "-dlt")
  @KafkaListener(topics = Topics.DELIVERY_PERSON_DELETE_RESPONSE)
  public void listenDeliveryPersonDeleted(String message) throws Exception {
    System.out.println("Received delivery person delete response");
    responseStore.putDeliveryPersonDelete(message);
  }

  @RetryableTopic(attempts = "4", backoff = @Backoff(delay = 2000), dltTopicSuffix = "-dlt")
  @KafkaListener(topics = Topics.DELIVERY_PERSON_DELETE_REQUESTED)
  public void listenDeliveryPersonDeleteRequested(String message) {
    System.out.println("Received delivery person delete requested");
    DeleteDeliveryPersonDto dto = Sender.fromJson(message, new TypeReference<>() {});
    userService.solveDeliveryPersonDeleteRequest(dto.getCorrelationId(), dto.getUsername());
  }

  @RetryableTopic(attempts = "4", backoff = @Backoff(delay = 2000), dltTopicSuffix = "-dlt")
  @KafkaListener(topics = Topics.DELIVERY_PERSON_SEARCH_BY_RESPONSE)
  public void listenDeliveryPersonSearch(String message) throws Exception {
    System.out.println("Received delivery person search response");
    responseStore.putDeliveryPersonSearch(message);
  }

  @KafkaListener(topics = Topics.MY_DELIVERIES_RESPONSE, groupId = "monolito-group")
  public void listenMyDeliveries(String message) throws Exception {
    System.out.println("Received my-deliveries-response");
    responseStore.putDeliveries(message);
  }

  @KafkaListener(topics = Topics.DELIVERY_DELIVERED, groupId = "monolito-group")
  public void listenDeliveryDelivered(String message) {
    System.out.println("Received delivery-delivered");
    Map<String, Object> payload = Sender.fromJson(message, new TypeReference<>() {});
    String username = payload.get("username").toString();
    Long orderId = Long.valueOf(payload.get("orderId").toString());
    orderService.markOrderAsDelivered(orderId);
    userService.markDeliveryPersonAsAvailable(username);
  }

  @KafkaListener(topics = Topics.DELIVERY_MARKED_DELIVERED_RESPONSE, groupId = "monolito-group")
  public void listenDeliveryMarkedDelivered(String message) throws Exception {
    System.out.println("Received delivery-marked-delivered response");
    responseStore.putDelivery(message);
  }

  @KafkaListener(topics = Topics.DELIVERY_PERSON_ASSIGNED, groupId = "monolito-group")
  public void listenDeliveryPersonAssigned(String message) throws Exception {
    System.out.println("Received delivery-person-assigned");
    responseStore.putDelivery(message);
  }

  @KafkaListener(topics = Topics.ASSIGN_DELIVERY_PERSON_RESPONSE, groupId = "monolito-group")
  public void listenAssignDeliveryPersonResponse(String message) throws Exception {
    System.out.println("Received assign-delivery-person-response");
    responseStore.putDelivery(message);
  }

  @KafkaListener(topics = Topics.CREATE_DELIVERY_RESPONSE, groupId = "monolito-group")
  public void listenCreateDeliveryResponse(String message) throws Exception {
    System.out.println("Received create-delivery-response");
    responseStore.putDelivery(message);
  }

  @DltHandler
  public void handleDlt(String requestMsg, @Header(KafkaHeaders.EXCEPTION_MESSAGE) String error) {
    System.out.println("Message sent to DLT after retries. Payload: " + requestMsg);
    System.out.println("Dead because: " + error);
  }
}
