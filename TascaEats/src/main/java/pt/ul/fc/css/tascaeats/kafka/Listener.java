package pt.ul.fc.css.tascaeats.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Component;
import pt.ul.fc.css.tascaeats.common.kafka.Topics;

@Component
public class Listener {

  @Autowired private KafkaResponseStore responseStore;

  @RetryableTopic(attempts = "4", backoff = @Backoff(delay = 2000), dltTopicSuffix = "-dlt")
  @KafkaListener(topics = Topics.DELIVERY_PERSON_REGISTER_RESPONSE)
  public void listenDeliveryPersonCreated(String message) throws Exception {

    System.out.println("Received delivery person registration response");
    responseStore.putDeliveryPerson(message);
  }

  @KafkaListener(topics = "my-deliveries-response", groupId = "monolito-group")
  public void listenMyDeliveries(String message) throws Exception {
    System.out.println("Received my-deliveries-response");
    responseStore.putDeliveries(message);
  }

  @KafkaListener(topics = "delivery-marked-delivered", groupId = "monolito-group")
  public void listenDeliveryMarkedDelivered(String message) throws Exception {
    System.out.println("Received delivery-marked-delivered");
    responseStore.putDelivery(message);
  }

  @DltHandler
  public void handleDlt(String requestMsg, @Header(KafkaHeaders.EXCEPTION_MESSAGE) String error) {
    System.out.println("Message sent to DLT after retries. Payload: " + requestMsg);
    System.out.println("Dead because: " + error);
  }
}
