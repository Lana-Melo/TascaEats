package pt.ul.fc.css.tascaeats.delivery.controller;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/delivery-persons")
@Api(value = "User API", tags = "Users")
public class DeliveryPersonController {

  /*
  NAO SEI SE PRECISAMOS DESTE CONTROLLER, AFINAL O MS POR ORA SÓ DEVE SER ACESSIVEL
  POR MEIO DO KAFKA VIA EVENTOS DO MONOLITO

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
        */
}
