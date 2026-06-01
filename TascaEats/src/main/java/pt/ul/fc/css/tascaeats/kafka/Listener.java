package pt.ul.fc.css.tascaeats.kafka;

import com.fasterxml.jackson.core.type.TypeReference;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import pt.ul.fc.css.tascaeats.services.OrderService;

@Component
public class Listener {

  @Autowired private KafkaResponseStore responseStore;

  @Autowired private OrderService orderService;

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

  @KafkaListener(topics = "delivery-delivered", groupId = "monolito-group")
  public void listenDeliveryDelivered(String message) {
    System.out.println("Received delivery-delivered");
    Map<String, Object> payload = Sender.fromJson(message, new TypeReference<>() {});
    Long orderId = Long.valueOf(payload.get("orderId").toString());
    orderService.markOrderAsDelivered(orderId);
  }
}
