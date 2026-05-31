package pt.ul.fc.css.tascaeats.delivery.kafka;

import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import pt.ul.fc.css.tascaeats.delivery.dto.request.DeliveryPersonRequestDto;
import pt.ul.fc.css.tascaeats.delivery.service.DeliveryPersonService;

@Component
public class Listener {

  @Autowired private DeliveryPersonService deliveryPersonService;

  @KafkaListener(topics = "register-delivery-person", groupId = "tascaeats-group")
  public void listenRegisterDeliveryPerson(String message) {

    DeliveryPersonRequestDto request =
        Sender.fromJson(message, new TypeReference<DeliveryPersonRequestDto>() {});

    System.out.println("Received delivery person event: " + request);
    deliveryPersonService.registerNewDeliveryPerson(request);
  }
}