package pt.ul.fc.css.tascaeats.common.dto;

import java.time.LocalDateTime;

public record DeliveryResponseDto(
    Long id,
    String status,
    Long orderId,
    LocalDateTime pickedUpTimeStamp,
    LocalDateTime deliveryTimeStamp,
    Long deliveryPersonId,
    String deliveryPersonName,
    String deliveryPersonNif) {}
