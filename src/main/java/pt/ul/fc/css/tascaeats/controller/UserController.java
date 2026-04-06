package pt.ul.fc.css.tascaeats.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import pt.ul.fc.css.tascaeats.dto.LoginRequest;
import pt.ul.fc.css.tascaeats.dto.UserRequest;
import pt.ul.fc.css.tascaeats.dto.UserResponse;

@RestController
@RequestMapping("/api/users")
@Api(value = "User API", tags = "Users")
public class UserController {
    /**
     * POST   /api/users/login              login (caso de uso A)
     * POST   /api/users/register           registo de utilizador - Client, Admin, DeliveryMan (caso de uso B)
     * GET    /api/users/{id}               verificar (caso de uso C)
     * PUT    /api/users/{id}               atualizar (caso de uso C)
     * DELETE /api/users/{id}               remover (caso de uso C)
     */
    
    //@Autowired
    //private UserService userHandler;   


    //POST   /api/users/login -> login (caso de uso A)
    //tenho de ir ate ao endpoint login e depois escrever no body um json com userName e password
    @PostMapping("/login") 
    @ApiOperation(value = "Login", notes = "Mock login, any password is accepted if user is valid.")
    public ResponseEntity<UserResponse> login(@RequestBody LoginRequest request) {
        UserResponse user = null; // chamar func login do UserService / handler (login(request.getUserName(), request.getPassword())
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build(); 
    }

    // Sofia, o service vai ter um metodo para cada tipo e o controller vai ter de ter
    // diferentes endpoints para registar cada tipo de user. (LANA)
    //POST   /api/users/register -> registo de utilizador - Client, Admin, DeliveryMan (caso de uso B)
    @PostMapping("/register")
    @ApiOperation(value = "Register user", notes = "Registers a new user (Client, Admin or DeliveryMan).")
    public ResponseEntity<UserResponse> register(@RequestBody UserRequest request) {
        UserResponse user = null; // chamar func register/create do UserService / handler (register(request))
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.badRequest().build(); //se n correr bem é pk os dados n sao validos (culpado é quem enviou os dados)
    }

    //GET    /api/users/{id} -> verificar (caso de uso C)
    @GetMapping("/{id}")
    @ApiOperation(value = "Get user by id", notes = "Returns a user given its id.")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long id) {
        UserResponse user = null; // chamar func getUser do UserService / handler (getUser(id)) ao inves de null claro
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    //PUT    /api/users/{id} -> atualizar (caso de uso C)
    @PutMapping("/{id}")
    @ApiOperation(value = "Update user", notes = "Updates an existing user.")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long id, @RequestBody UserRequest request) {
        UserResponse user = null; // chamar func updateUser do UserService / handler (updateUser(id, request))
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    //DELETE /api/users/{id} -> remover (caso de uso C)
    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete user", notes = "Removes an existing user.")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        // chamar func deleteUser do UserService / handler (deleteUser(id))
        // se correr bem, devolve no content, se n encontrar o user devolve not found - o deleteUser do service pode devolver boolean ou entao pode lançar exceção se n encontrar o user
        return ResponseEntity.noContent().build(); 
    }
}

