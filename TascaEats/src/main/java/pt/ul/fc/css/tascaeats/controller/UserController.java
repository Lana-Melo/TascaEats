package pt.ul.fc.css.tascaeats.controller;

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
import pt.ul.fc.css.tascaeats.dto.users.request.ClientRequestDto;
import pt.ul.fc.css.tascaeats.dto.users.request.DeliveryPersonRequestDto;
import pt.ul.fc.css.tascaeats.dto.users.request.LoginRequestDto;
import pt.ul.fc.css.tascaeats.dto.users.request.UserRequestDto;
import pt.ul.fc.css.tascaeats.dto.users.response.ClientResponseDto;
import pt.ul.fc.css.tascaeats.dto.users.response.DeliveryPersonResponseDto;
import pt.ul.fc.css.tascaeats.dto.users.response.UserResponseDto;
import pt.ul.fc.css.tascaeats.services.UserService;

@RestController
@RequestMapping("/api/users")
@Api(value = "User API", tags = "Users")
public class UserController {

  private final UserService userHandler;

  public UserController(UserService userService) {
    this.userHandler = userService;
  }

  @PostMapping("/login")
  @ApiOperation(value = "Login", notes = "Mock login, any password is accepted if user is valid.")
  public ResponseEntity<UserResponseDto> login(@RequestBody @Valid LoginRequestDto request) {
    UserResponseDto user = userHandler.login(request.getUsername(), request.getPassword());
    return ResponseEntity.ok(user);
  }

  @PostMapping("/register/client")
  @ApiOperation(value = "Register client", notes = "Register a new client")
  public ResponseEntity<ClientResponseDto> registerClient(
      @RequestBody @Valid ClientRequestDto request) {
    ClientResponseDto user = userHandler.registerNewClient(request);
    return ResponseEntity.ok(user);
  }

  @PostMapping("/register/admin")
  @ApiOperation(value = "Register admin", notes = "Register a new admin")
  public ResponseEntity<UserResponseDto> registerAdmin(@RequestBody @Valid UserRequestDto request) {
    UserResponseDto user = userHandler.registerNewAdmin(request);
    return ResponseEntity.ok(user);
  }

  @PostMapping("/register/delivery")
  @ApiOperation(value = "Register delivery person", notes = "Register a new delivery person")
  public ResponseEntity<DeliveryPersonResponseDto> registerDelivery(
      @RequestBody @Valid DeliveryPersonRequestDto request) {
    DeliveryPersonResponseDto user = userHandler.registerNewDeliveryPerson(request);
    return ResponseEntity.ok(user);
  }

  @GetMapping("/{id}")
  @ApiOperation(value = "Get user", notes = "Returns a user for a given id.")
  public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id) {
    UserResponseDto user = userHandler.getUserById(id);
    return ResponseEntity.ok(user);
  }

  @GetMapping("/search")
  @ApiOperation(value = "Search users", notes = "Returns a list of users filtered by a given id.")
  public ResponseEntity<List<UserResponseDto>> searchUser(
      @RequestParam(required = false) String name,
      @RequestParam(required = false) String userType,
      @RequestParam(required = false) String orderNumber,
      @RequestParam(required = false) String deliveryNumber) {
    List<UserResponseDto> users = userHandler.searchBy(name, userType, orderNumber, deliveryNumber);
    return ResponseEntity.ok(users);
  }

  @PutMapping("/client/{id}")
  @ApiOperation(value = "Update client", notes = "Updates an existing client.")
  public ResponseEntity<ClientResponseDto> updateClient(
      @PathVariable Long id, @RequestBody @Valid ClientRequestDto request) {
    ClientResponseDto client = userHandler.updateClient(id, request);
    return ResponseEntity.ok(client);
  }

  @PutMapping("/admin/{id}")
  @ApiOperation(value = "Update admin", notes = "Updates an existing admin.")
  public ResponseEntity<UserResponseDto> updateAdmin(
      @PathVariable Long id, @RequestBody @Valid UserRequestDto request) {
    UserResponseDto admin = userHandler.updateAdmin(id, request);
    return ResponseEntity.ok(admin);
  }

  @PutMapping("/delivery/{id}")
  @ApiOperation(value = "Update delivery person", notes = "Updates an existing delivery person.")
  public ResponseEntity<DeliveryPersonResponseDto> updateDeliveryPerson(
      @PathVariable Long id, @RequestBody @Valid DeliveryPersonRequestDto request) {
    DeliveryPersonResponseDto deliveryPerson = userHandler.updateDeliveryPerson(id, request);
    return deliveryPerson != null
        ? ResponseEntity.ok(deliveryPerson)
        : ResponseEntity.notFound().build();
  }

  @DeleteMapping("/{id}")
  @ApiOperation(value = "Delete user", notes = "Removes an existing user.")
  public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
    return userHandler.deleteUser(id)
        ? ResponseEntity.noContent().build()
        : ResponseEntity.notFound().build();
  }
}
