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
import pt.ul.fc.css.tascaeats.common.kafka.Topics;
import pt.ul.fc.css.tascaeats.services.OrderService;

@Component
public class Listener {

  @Autowired private KafkaResponseStore responseStore;

  @Autowired private OrderService orderService;

  @RetryableTopic(attempts = "4", backoff = @Backoff(delay = 2000), dltTopicSuffix = "-dlt")
  @KafkaListener(topics = Topics.DELIVERY_PERSON_REGISTER_RESPONSE)
  public void listenDeliveryPersonCreated(String message) throws Exception {
    System.out.println("Received delivery person registration response");
    // desbloqueia o servico que está à espera da resposta do registo de delivery person
    responseStore.putDeliveryPerson(message);
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
    Long orderId = Long.valueOf(payload.get("orderId").toString());
    orderService.markOrderAsDelivered(orderId);
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

  @DltHandler
  public void handleDlt(String requestMsg, @Header(KafkaHeaders.EXCEPTION_MESSAGE) String error) {
    System.out.println("Message sent to DLT after retries. Payload: " + requestMsg);
    System.out.println("Dead because: " + error);
  }
}
