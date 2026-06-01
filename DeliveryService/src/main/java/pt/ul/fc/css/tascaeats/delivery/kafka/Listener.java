package pt.ul.fc.css.tascaeats.delivery.kafka;

import com.fasterxml.jackson.core.type.TypeReference;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import pt.ul.fc.css.tascaeats.delivery.dto.request.DeliveryPersonRequestDto;
import pt.ul.fc.css.tascaeats.delivery.dto.response.DeliveryResponseDto;
import pt.ul.fc.css.tascaeats.delivery.service.DeliveryPersonService;
import pt.ul.fc.css.tascaeats.delivery.service.DeliveryService;

@Component
public class Listener {

  @Autowired private DeliveryPersonService deliveryPersonService;

  @Autowired private DeliveryService deliveryService;

  @Autowired private Sender sender;

  @KafkaListener(topics = "register-delivery-person", groupId = "tascaeats-group")
  public void listenRegisterDeliveryPerson(String message) {

    DeliveryPersonRequestDto request =
        Sender.fromJson(message, new TypeReference<DeliveryPersonRequestDto>() {});

    System.out.println("Received delivery person event: " + request);
    deliveryPersonService.registerNewDeliveryPerson(request);
  }

  @KafkaListener(topics = "get-my-deliveries", groupId = "tascaeats-group")
  public void listenGetMyDeliveries(String message) {
    Long deliveryPersonId = Sender.fromJson(message, new TypeReference<Long>() {});
    System.out.println("Received get-my-deliveries for deliveryPersonId: " + deliveryPersonId);
    List<DeliveryResponseDto> deliveries =
        deliveryService.getDeliveriesByDeliveryPerson(deliveryPersonId);
    sender.sendAsJson("my-deliveries-response", deliveries);
  }

  @KafkaListener(topics = "mark-delivery-delivered", groupId = "tascaeats-group")
  public void listenMarkDelivered(String message) {
    Long deliveryId = Sender.fromJson(message, new TypeReference<Long>() {});
    System.out.println("Received mark-delivery-delivered for deliveryId: " + deliveryId);
    deliveryService.updateDeliveryStatus(deliveryId);
    // o updateDeliveryStatus do servico faz o delivery delivered
  }
}
