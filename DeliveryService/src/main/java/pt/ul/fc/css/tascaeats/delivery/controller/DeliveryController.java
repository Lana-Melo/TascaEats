package pt.ul.fc.css.tascaeats.delivery.controller;

import io.swagger.annotations.ApiOperation;
import pt.ul.fc.css.tascaeats.delivery.dto.response.DeliveryResponseDto;
import pt.ul.fc.css.tascaeats.delivery.service.DeliveryService;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/deliveries")
public class DeliveryController {

  private final DeliveryService deliveryHandler;

  public DeliveryController(DeliveryService deliveryService) {
    this.deliveryHandler = deliveryService;
  }

  @PostMapping
  @ApiOperation(value = "Create delivery", notes = "Creates a new delivery.")
  public ResponseEntity<DeliveryResponseDto> createDelivery(@PathVariable Long orderId) {
    DeliveryResponseDto delivery = deliveryHandler.createDelivery(orderId);
    return ResponseEntity.ok(delivery);
  }

  @PatchMapping("/{id}/status")
  @ApiOperation(value = "Update delivery status", notes = "Updates the status of a delivery.")
  public ResponseEntity<DeliveryResponseDto> updateDeliveryStatus(
      @PathVariable Long id) {
    DeliveryResponseDto delivery = deliveryHandler.updateDeliveryStatus(id);
    return ResponseEntity.ok(delivery);
  }

  @PatchMapping("/{id}/deliveryPerson/{deliveryPersonId}")
  @ApiOperation(value = "Assign delivery person", notes = "Assigns a delivery person to a delivery.")
  public ResponseEntity<DeliveryResponseDto> assignDeliveryPerson(
      @PathVariable Long id, @PathVariable Long deliveryPersonId) {
    DeliveryResponseDto delivery = deliveryHandler.assignDeliveryPerson(id, deliveryPersonId);
    return ResponseEntity.ok(delivery);
  }

  @GetMapping
  @ApiOperation(value = "Get all deliveries", notes = "Returns a list of all deliveries.")
  public ResponseEntity<List<DeliveryResponseDto>> getAllDeliveries() {
    return ResponseEntity.ok(deliveryHandler.getAllDeliveries());
  }
}
