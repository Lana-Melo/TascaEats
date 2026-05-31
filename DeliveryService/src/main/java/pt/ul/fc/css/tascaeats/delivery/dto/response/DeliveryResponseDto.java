package pt.ul.fc.css.tascaeats.delivery.dto.response;

import java.time.LocalDateTime;
import pt.ul.fc.css.tascaeats.delivery.model.DeliveryStatus;

public record DeliveryResponseDto(
    Long id,
    DeliveryStatus status,
    Long orderId,
    LocalDateTime pickedUpTimeStamp,
    LocalDateTime deliveryTimeStamp,
    Long deliveryPersonId,
    String deliveryPersonName,
    String deliveryPersonNif) {}
