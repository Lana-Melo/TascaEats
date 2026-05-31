package pt.ul.fc.css.tascaeats.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class Listener {

  @Autowired private KafkaResponseStore responseStore;

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
}
