package pt.ul.fc.css.tascaeats.delivery.util;

import org.springframework.stereotype.Component;
import pt.ul.fc.css.tascaeats.delivery.dto.DeliveryPersonResponseDto;
import pt.ul.fc.css.tascaeats.delivery.model.DeliveryPerson;

@Component
public class Mapper {

  public static DeliveryPersonResponseDto mapDeliveryToDeliveryPersonResponse(
      DeliveryPerson deliveryPerson) {
    return new DeliveryPersonResponseDto(
        deliveryPerson.getId(),
        deliveryPerson.getUsername(),
        deliveryPerson.getName(),
        deliveryPerson.getPhoneNumber(),
        deliveryPerson.getNif(),
        deliveryPerson.getDeliveryPersonStatus().toString());
  }
}
