package pt.ul.fc.css.tascaeats.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.ApiOperation;
import jakarta.validation.Valid;
import pt.ul.fc.css.tascaeats.dto.order.OrderRequest;
import pt.ul.fc.css.tascaeats.dto.order.OrderResponse;
import pt.ul.fc.css.tascaeats.dto.order.PaymentRequest;
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
    public ResponseEntity<OrderResponse> createOrder(@RequestBody @Valid OrderRequest request) {
        OrderResponse order = orderHandler.createOrder(request);
        return order != null ? ResponseEntity.ok(order) : ResponseEntity.badRequest().build();
    }

    @PostMapping("/payment")
    @ApiOperation(value = "Process payment", notes = "Registers and processes payment of an order.")
    public ResponseEntity<OrderResponse> processPayment(@RequestBody @Valid PaymentRequest request) {
        OrderResponse order = orderHandler.processPayment(request);
        return order != null ? ResponseEntity.ok(order) : ResponseEntity.badRequest().build();
    }

    @PatchMapping("/{id}/status")
    @ApiOperation(value = "Update order status", notes = "Updates the status of an order.")
    public ResponseEntity<OrderResponse> updateOrderStatus(@PathVariable Long id, @RequestParam OrderStatus status) {
        OrderResponse order = orderHandler.updateOrderStatus(id, status.toString()); 
        return order != null ? ResponseEntity.ok(order) : ResponseEntity.badRequest().build();
    }

    @PatchMapping("/{id}/deliveryPerson/{deliveryPersonId}")
    @ApiOperation(value = "Assign delivery person", notes = "Assigns a delivery person to an order.")
    public ResponseEntity<OrderResponse> assignDeliveryPerson(@PathVariable Long id, @PathVariable Long deliveryPersonId) {
        OrderResponse order = orderHandler.assignDeliveryPerson(id, deliveryPersonId);
        return order != null ? ResponseEntity.ok(order) : ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Cancel order", notes = "Cancels an order.")
    public ResponseEntity<Void> cancelOrder(@PathVariable Long id) {
        return orderHandler.cancelOrder(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
