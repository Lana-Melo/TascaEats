package pt.ul.fc.css.tascaeats.controller;

import org.springframework.stereotype.Controller;

import pt.ul.fc.css.tascaeats.services.OrderService;
import pt.ul.fc.css.tascaeats.services.RestaurantService;
import pt.ul.fc.css.tascaeats.services.UserService;

@Controller
public class WebController {

    private final UserService userService;
    private final RestaurantService restaurantService;
    private final OrderService orderService;

    public WebController(UserService userService,
                        RestaurantService restaurantService,
                        OrderService orderService) {
        this.userService = userService;
        this.restaurantService = restaurantService;
        this.orderService = orderService;
    }

    // ------- USER -------

    //UC A - login

    //UC B - registar clientes 

    //---------------------
    

    // ---- RESTAURANT ----

    //UC E - buscar por restaurantes

    //UC I - buscar produtos de um restaurante

    //---------------------


    // ------- ORDER -------

    //UC J - criar pedido

    //UC N - cancelar pedido 

    //---------------------


    
}
