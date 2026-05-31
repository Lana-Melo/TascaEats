package pt.ul.fc.css.tascaeats.delivery.util;

import org.springframework.stereotype.Component;
import pt.ul.fc.css.tascaeats.delivery.dto.response.DeliveryPersonResponseDto;
import pt.ul.fc.css.tascaeats.delivery.dto.response.DeliveryResponseDto;
import pt.ul.fc.css.tascaeats.delivery.model.Delivery;
import pt.ul.fc.css.tascaeats.delivery.model.DeliveryPerson;

@Component
public class Mapper {

  public static DeliveryPersonResponseDto mapDeliveryPersonToDeliveryPersonResponse(
      DeliveryPerson deliveryPerson) {
    return new DeliveryPersonResponseDto(
        deliveryPerson.getId(),
        deliveryPerson.getUsername(),
        deliveryPerson.getName(),
        deliveryPerson.getPhoneNumber(),
        deliveryPerson.getNif(),
        deliveryPerson.getDeliveryPersonStatus().toString());
  }

  public static DeliveryResponseDto mapDeliveryToDeliveryResponse(Delivery delivery) {
    Long delId = delivery.getDeliveryPerson() == null ? null : delivery.getDeliveryPerson().getId();
    String delName =
        delivery.getDeliveryPerson() == null ? null : delivery.getDeliveryPerson().getName();
    String delNif =
        delivery.getDeliveryPerson() == null ? null : delivery.getDeliveryPerson().getNif();

    return new DeliveryResponseDto(
        delivery.getId(),
        delivery.getStatus(),
        delivery.getOrderId(),
        delivery.getPickedUpTimeStamp(),
        delivery.getDeliveryTimeStamp(),
        delId,
        delName,
        delNif);
  }
}
