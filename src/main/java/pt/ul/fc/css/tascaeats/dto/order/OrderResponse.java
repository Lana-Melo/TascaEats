package pt.ul.fc.css.tascaeats.dto.order;

import java.time.LocalDateTime;
import java.util.List;

import pt.ul.fc.css.tascaeats.entities.enums.OrderStatus;

public record OrderResponse(
    Long id, //gerado pelo spring 
    OrderStatus status,
    LocalDateTime timestamp,
    double totalValue,
    Long clientId,
    String clientName,
    Long restaurantId,
    String restaurantDesignation,
    List<Long> productIds,
    Long deliveryPersonId,
    String deliveryPersonName
){}
