package pt.ul.fc.css.tascaeats.dto.order;

import java.time.LocalDateTime;
import java.util.List;
import pt.ul.fc.css.tascaeats.entities.enums.OrderStatus;

public record OrderResponseDto(
    Long id,
    OrderStatus status,
    LocalDateTime timestamp,
    double totalValue,
    Long clientId,
    String clientName,
    String clientNif,
    List<Long> restaurantIds,
    List<Long> productIds,
    List<OrderProductItemDto> products,
    Long deliveryPersonId,
    String deliveryPersonName,
    String deliveryPersonNif,
    String city,
    String street,
    String postalCode,
    Double paymentAmount,
    LocalDateTime paymentTimestamp,
    Double paymentChange) {}
