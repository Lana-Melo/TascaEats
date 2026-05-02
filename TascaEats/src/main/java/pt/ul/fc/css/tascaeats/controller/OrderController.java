package pt.ul.fc.css.tascaeats.controller;

import io.swagger.annotations.ApiOperation;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pt.ul.fc.css.tascaeats.dto.order.OrderRequestDto;
import pt.ul.fc.css.tascaeats.dto.order.OrderResponseDto;
import pt.ul.fc.css.tascaeats.dto.order.PaymentRequestDto;
import pt.ul.fc.css.tascaeats.entities.enums.OrderStatus;
import pt.ul.fc.css.tascaeats.services.OrderService;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

  private final OrderService orderHandler;

  public OrderController(OrderService orderService) {
    this.orderHandler = orderService;
  }

  @PostMapping
  @ApiOperation(value = "Create order", notes = "Creates a new order.")
  public ResponseEntity<OrderResponseDto> createOrder(@RequestBody @Valid OrderRequestDto request) {
    OrderResponseDto order = orderHandler.createOrder(request);
    return ResponseEntity.ok(order);
  }

  @PostMapping("/payment")
  @ApiOperation(value = "Process payment", notes = "Registers and processes payment of an order.")
  public ResponseEntity<OrderResponseDto> processPayment(
      @RequestBody @Valid PaymentRequestDto request) {
    OrderResponseDto order = orderHandler.processPayment(request);
    return ResponseEntity.ok(order);
  }

  @PatchMapping("/{id}/status")
  @ApiOperation(value = "Update order status", notes = "Updates the status of an order.")
  public ResponseEntity<OrderResponseDto> updateOrderStatus(
      @PathVariable Long id, @RequestParam OrderStatus status) {
    OrderResponseDto order = orderHandler.updateOrderStatus(id, status.toString());
    return ResponseEntity.ok(order);
  }

  @PatchMapping("/{id}/deliveryPerson/{deliveryPersonId}")
  @ApiOperation(value = "Assign delivery person", notes = "Assigns a delivery person to an order.")
  public ResponseEntity<OrderResponseDto> assignDeliveryPerson(
      @PathVariable Long id, @PathVariable Long deliveryPersonId) {
    OrderResponseDto order = orderHandler.assignDeliveryPerson(id, deliveryPersonId);
    return ResponseEntity.ok(order);
  }

  @DeleteMapping("/{id}")
  @ApiOperation(value = "Cancel order", notes = "Cancels an order.")
  public ResponseEntity<Void> cancelOrder(@PathVariable Long id) {
    return orderHandler.cancelOrder(id)
        ? ResponseEntity.noContent().build()
        : ResponseEntity.notFound().build();
  }
}
