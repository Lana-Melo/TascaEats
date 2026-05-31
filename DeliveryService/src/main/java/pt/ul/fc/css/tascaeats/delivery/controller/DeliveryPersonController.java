package pt.ul.fc.css.tascaeats.delivery.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pt.ul.fc.css.tascaeats.delivery.dto.request.DeliveryPersonRequestDto;
import pt.ul.fc.css.tascaeats.delivery.dto.response.DeliveryPersonResponseDto;
import pt.ul.fc.css.tascaeats.delivery.service.DeliveryPersonService;

@RestController
@RequestMapping("/api/delivery-persons")
@Api(value = "User API", tags = "Users")
public class DeliveryPersonController {

  private final DeliveryPersonService DeliveryPersonHandler;

  public DeliveryPersonController(DeliveryPersonService deliveryPersonService) {
    this.DeliveryPersonHandler = deliveryPersonService;
  }

  @PostMapping("/register")
  @ApiOperation(value = "Register delivery person", notes = "Register a new delivery person")
  public ResponseEntity<Void> registerDelivery(
      @RequestBody @Valid DeliveryPersonRequestDto request) {
    DeliveryPersonHandler.registerNewDeliveryPerson(request);
    return ResponseEntity.accepted().build();
  }

  @GetMapping("/{id}")
  @ApiOperation(value = "Get user", notes = "Returns a user for a given id.")
  public ResponseEntity<DeliveryPersonResponseDto> getUserById(@PathVariable Long id) {
    DeliveryPersonResponseDto user = DeliveryPersonHandler.getUserById(id);
    return ResponseEntity.ok(user);
  }

  @GetMapping("/search")
  @ApiOperation(value = "Search users", notes = "Returns a list of users filtered by a given id.")
  public ResponseEntity<List<DeliveryPersonResponseDto>> searchUser(
      @RequestParam(required = false) String name,
      @RequestParam(required = false) String userType,
      @RequestParam(required = false) String delStatus,
      @RequestParam(required = false) String deliveryNumber) {
    List<DeliveryPersonResponseDto> users =
        DeliveryPersonHandler.searchBy(name, userType, delStatus, deliveryNumber);
    return ResponseEntity.ok(users);
  }

  @PutMapping("/{id}")
  @ApiOperation(value = "Update delivery person", notes = "Updates an existing delivery person.")
  public ResponseEntity<DeliveryPersonResponseDto> updateDeliveryPerson(
      @PathVariable Long id, @RequestBody @Valid DeliveryPersonRequestDto request) {
    DeliveryPersonResponseDto deliveryPerson =
        DeliveryPersonHandler.updateDeliveryPerson(id, request);
    return ResponseEntity.ok(deliveryPerson);
  }

  @DeleteMapping("/{id}")
  @ApiOperation(value = "Delete user", notes = "Removes an existing user.")
  public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
    return DeliveryPersonHandler.deleteDeliveryPerson(id)
        ? ResponseEntity.noContent().build()
        : ResponseEntity.notFound().build();
  }
}
