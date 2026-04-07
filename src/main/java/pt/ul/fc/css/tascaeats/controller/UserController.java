package pt.ul.fc.css.tascaeats.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.validation.Valid;
import pt.ul.fc.css.tascaeats.dto.users.request.ClientRequest;
import pt.ul.fc.css.tascaeats.dto.users.request.DeliveryPersonRequest;
import pt.ul.fc.css.tascaeats.dto.users.request.LoginRequest;
import pt.ul.fc.css.tascaeats.dto.users.request.UserRequest;
import pt.ul.fc.css.tascaeats.dto.users.response.ClientResponse;
import pt.ul.fc.css.tascaeats.dto.users.response.DeliveryPersonResponse;
import pt.ul.fc.css.tascaeats.dto.users.response.UserResponse;
import pt.ul.fc.css.tascaeats.services.UserService;

@RestController
@RequestMapping("/api/users")
@Api(value = "User API", tags = "Users")
public class UserController {

    private final UserService userHandler;   

    public UserController(UserService userService){
        this.userHandler = userService;
    }

    @PostMapping("/login") 
    @ApiOperation(value = "Login", notes = "Mock login, any password is accepted if user is valid.")
    public ResponseEntity<UserResponse> login(@RequestBody @Valid LoginRequest request) {
        UserResponse user = userHandler.login(request.getUserName(), request.getPassword());
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build(); 
    }

    @PostMapping("/register/client")
    @ApiOperation(value = "Register client", notes = "Register a new client")
    public ResponseEntity<ClientResponse> registerClient(@RequestBody @Valid ClientRequest request) {
        ClientResponse user = userHandler.registerNewClient(request);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.badRequest().build();
    }

    @PostMapping("/register/admin")
    @ApiOperation(value = "Register admin", notes = "Register a new admin")
    public ResponseEntity<UserResponse> registerAdmin(@RequestBody @Valid UserRequest request) {
        UserResponse user = userHandler.registerNewAdmin(request);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.badRequest().build();
    }

    @PostMapping("/register/delivery")
    @ApiOperation(value = "Register delivery person", notes = "Register a new delivery person")
    public ResponseEntity<DeliveryPersonResponse> registerDelivery(@RequestBody @Valid DeliveryPersonRequest request) {
        DeliveryPersonResponse user = userHandler.registerNewDeliveryPerson(request);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.badRequest().build();
    }

    @PutMapping("/client/{id}")
    @ApiOperation(value = "Update client", notes = "Updates an existing client.")
    public ResponseEntity<ClientResponse> updateClient(@PathVariable Long id, @RequestBody @Valid ClientRequest request) {
        ClientResponse client = userHandler.updateClient(id, request);
        return client != null ? ResponseEntity.ok(client) : ResponseEntity.notFound().build();
    }

    @PutMapping("/admin/{id}")
    @ApiOperation(value = "Update admin", notes = "Updates an existing admin.")
    public ResponseEntity<UserResponse> updateAdmin(@PathVariable Long id, @RequestBody @Valid UserRequest request) {
        UserResponse admin = userHandler.updateAdmin(id, request);
        return admin != null ? ResponseEntity.ok(admin) : ResponseEntity.notFound().build();
    }

    @PutMapping("/delivery/{id}")
    @ApiOperation(value = "Update delivery person", notes = "Updates an existing delivery person.")
    public ResponseEntity<DeliveryPersonResponse> updateDeliveryPerson(@PathVariable Long id, @RequestBody @Valid DeliveryPersonRequest request) {
        DeliveryPersonResponse deliveryPerson = userHandler.updateDeliveryPerson(id, request);
        return deliveryPerson != null ? ResponseEntity.ok(deliveryPerson) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete user", notes = "Removes an existing user.")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        return userHandler.deleteUser(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}