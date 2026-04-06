package pt.ul.fc.css.tascaeats.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties.Admin;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import pt.ul.fc.css.tascaeats.dto.users.ClientRequest;
import pt.ul.fc.css.tascaeats.dto.users.ClientResponse;
import pt.ul.fc.css.tascaeats.dto.users.DeliveryPersonRequest;
import pt.ul.fc.css.tascaeats.dto.users.DeliveryPersonResponse;
import pt.ul.fc.css.tascaeats.dto.users.LoginRequest;
import pt.ul.fc.css.tascaeats.dto.users.UserRequest;
import pt.ul.fc.css.tascaeats.dto.users.UserResponse;
import pt.ul.fc.css.tascaeats.services.UserService;

@RestController
@RequestMapping("/api/users")
@Api(value = "User API", tags = "Users")
public class UserController {
    /**
     * POST   /api/users/login              login (caso de uso A)
     * POST   /api/users/client/register     Registo de Client (caso de uso B)
     * POST   /api/users/admin/register       Registo de Admin (caso de uso B)
     * POST   /api/users/delivery/register    Registo de DeliveryPerson (caso de uso B)
     * GET    /api/users/client/{id}               verificar (caso de uso C)
     * GET    /api/users/delivery/{id}               verificar (caso de uso C)
     * GET    /api/users/admin/{id}               verificar (caso de uso C)
     * PUT    /api/users/client/{id}               atualizar (caso de uso C)
     * PUT    /api/users/delivery/{id}               atualizar (caso de uso C)
     * PUT    /api/users/admin/{id}               atualizar (caso de uso C)
     * DELETE /api/users/{id}               remover (caso de uso C)
     */
    
    @Autowired
    private UserService userHandler;   

    //POST   /api/users/login -> login (caso de uso A)
    //tenho de ir ate ao endpoint login e depois escrever no body um json com userName e password
    @PostMapping("/login") 
    @ApiOperation(value = "Login", notes = "Mock login, any password is accepted if user is valid.")
    public ResponseEntity<UserResponse> login(@RequestBody LoginRequest request) {
        UserResponse user = userHandler.login(request.getUserName(), request.getPassword()); // chamar func login do UserService / handler (login(request.getUserName(), request.getPassword())
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build(); 
    }

    //POST   /api/users/register/client -> registo de cliente (caso de uso B)
    @PostMapping("/register/client")
    @ApiOperation(value = "Register client", notes = "Register a new client")
    public ResponseEntity<ClientResponse> registerClient(@RequestBody ClientRequest request) {
        ClientResponse user = userHandler.registerNewClient(request);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.badRequest().build();
    }

    //POST   /api/users/register/admin -> registo de admin (caso de uso B)
    @PostMapping("/register/admin")
    @ApiOperation(value = "Register admin", notes = "Register a new admin")
    public ResponseEntity<UserResponse> registerAdmin(@RequestBody UserRequest request) {
        UserResponse user = userHandler.registerNewAdmin(request);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.badRequest().build();
    }

    //POST   /api/users/register/delivery -> registo de delivery person (caso de uso B)
    @PostMapping("/register/delivery")
    @ApiOperation(value = "Register delivery person", notes = "Register a new delivery person")
    public ResponseEntity<DeliveryPersonResponse> registerDelivery(@RequestBody DeliveryPersonRequest request) {
        DeliveryPersonResponse user = userHandler.registerNewDeliveryPerson(request);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.badRequest().build();
    }

    //PUT    /api/users/client/{id} -> atualizar (caso de uso C)
    @PutMapping("/client/{id}")
    @ApiOperation(value = "Update client", notes = "Updates an existing client.")
    public ResponseEntity<ClientResponse> updateClient(@PathVariable Long id, @RequestBody ClientRequest request) {
        ClientResponse client = userHandler.updateClient(id, request);
        return client != null ? ResponseEntity.ok(client) : ResponseEntity.notFound().build();
    }

    //PUT    /api/users/admin/{id} -> atualizar (caso de uso C)
    @PutMapping("/admin/{id}")
    @ApiOperation(value = "Update admin", notes = "Updates an existing admin.")
    public ResponseEntity<UserResponse> updateAdmin(@PathVariable Long id, @RequestBody UserRequest request) {
        UserResponse admin = userHandler.updateAdmin(id, request);
        return admin != null ? ResponseEntity.ok(admin) : ResponseEntity.notFound().build();
    }

    //PUT    /api/users/delivery/{id} -> atualizar (caso de uso C)
    @PutMapping("/delivery/{id}")
    @ApiOperation(value = "Update delivery person", notes = "Updates an existing delivery person.")
    public ResponseEntity<DeliveryPersonResponse> updateDeliveryPerson(@PathVariable Long id, @RequestBody DeliveryPersonRequest request) {
        DeliveryPersonResponse deliveryPerson = userHandler.updateDeliveryPerson(id, request);
        return deliveryPerson != null ? ResponseEntity.ok(deliveryPerson) : ResponseEntity.notFound().build();
    }

    //DELETE /api/users/{id} -> remover (caso de uso C)
    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete user", notes = "Removes an existing user.")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        return userHandler.deleteUser(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}

