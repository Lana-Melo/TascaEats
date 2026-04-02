package pt.ul.fc.css.tascaeats.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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


    //caso de uso A login 
    @PostMapping("/login")
    @ApiOperation(value = "Login", notes = "Mock login, any password is accepted if user is valid.")
    public ResponseEntity<UserResponse> login(@RequestParam String userName, @RequestParam String password) {
        return ResponseEntity.ok(new UserResponse());
    }

    
}
