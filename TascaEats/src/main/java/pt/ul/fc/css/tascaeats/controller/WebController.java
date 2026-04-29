package pt.ul.fc.css.tascaeats.controller;

import jakarta.servlet.http.HttpSession;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pt.ul.fc.css.tascaeats.dto.order.OrderRequest;
import pt.ul.fc.css.tascaeats.dto.users.request.ClientRequest;
import pt.ul.fc.css.tascaeats.dto.users.response.UserResponse;
import pt.ul.fc.css.tascaeats.services.OrderService;
import pt.ul.fc.css.tascaeats.services.RestaurantService;
import pt.ul.fc.css.tascaeats.services.UserService;

@Controller
public class WebController {

  private final UserService userService;
  private final RestaurantService restaurantService;
  private final OrderService orderService;

  public WebController(
      UserService userService, RestaurantService restaurantService, OrderService orderService) {
    this.userService = userService;
    this.restaurantService = restaurantService;
    this.orderService = orderService;
  }

  // ------- USER -------

  // UC A - login

  @GetMapping("/login")
  public String loadsLoginPage() {
    return "login"; // returns the login.html template
  }

  @PostMapping("/login")
  public String login(
      @RequestParam String username,
      @RequestParam String password,
      HttpSession session,
      Model model,
      RedirectAttributes ra) {
    try {
      UserResponse userDto =
          userService.login(
              username,
              password); // apesar de ser um cliente, o loginn retorna UserRequest mas n faz mal n
      // precisamos da morada
      session.setAttribute("userId", userDto.id());
      // session.setAttribute("userType", userDto.userType()); //nao sei, talvez seja preciso
      ra.addFlashAttribute("message", "Login successful!");
      return "redirect:/restaurants"; // quem fizer login pela interface web vai sempre para os
      // restaurantes

    } catch (
        RuntimeException
            ex) { // resouce not found exception é apanhada aqui na mesma, assim excusamos de
      // importar
      model.addAttribute("error", ex.getMessage());
      return "login";
    }
  }

  // UC B - registar clientes

  @GetMapping("/register")
  public String registerPage() {
    return "register"; // returns the register.html template
  }

  @PostMapping("/register")
  public String register(
      @RequestParam String username,
      @RequestParam String password,
      @RequestParam String name,
      @RequestParam String phoneNumber,
      @RequestParam String nif,
      @RequestParam
          List<List<String>> addresses, // nao sei se pode ser só assim, depois voltar aqui :)
      Model model,
      RedirectAttributes ra) {
    try {
      ClientRequest clientRequest = new ClientRequest();
      clientRequest.setUsername(username);
      clientRequest.setPassword(password);
      clientRequest.setName(name);
      clientRequest.setPhoneNumber(phoneNumber);
      clientRequest.setNif(nif);
      clientRequest.setAddresses(addresses);
      userService.registerNewClient(clientRequest);
      ra.addFlashAttribute("message", "Client registered successfully!");
      return "redirect:/login"; // returns the login.html template
    } catch (IllegalArgumentException ex) {
      model.addAttribute("error", ex.getMessage());
      return "register";
    }
  }

  // ---------------------

  // ---- RESTAURANT ----

  // UC E - buscar por restaurantes
  @GetMapping("/restaurants")
  public String restaurants(Model model) {
    // model.addAttribute("restaurants", restaurantService.getAllRestaurants());
    return "restaurants"; // returns the restaurants.html template
  }

  @GetMapping("/restaurants/search")
  public String searchRestaurants(@RequestParam String query, Model model) {
    // model.addAttribute("restaurants", restaurantService.searchRestaurants(query));
    return "restaurants"; // returns the restaurants.html template
    // voltar aqui o client tem q escolher o filtro q deseja, se quer filtar por nome, cidade, tipo
    // de comida lalalal
  }

  // UC I - buscar produtos de um restaurante
  @GetMapping("/restaurants/{restaurantId}/products")
  public String restaurantProducts(
      @RequestParam Long restaurantId, Model model, RedirectAttributes ra) {
    try {
      model.addAttribute("products", restaurantService.getProducts(restaurantId));
      return "products"; // returns the products.html template
    } catch (RuntimeException ex) {
      ra.addFlashAttribute("error", ex.getMessage());
      return "redirect:/restaurants";
    }
  }

  // ---------------------

  // ------- ORDER -------

  // UC J - criar pedido
  @PostMapping("/orders")
  public String createOrder(
      @RequestParam Long restaurantId,
      @RequestParam List<Long> productIds,
      @RequestParam String city,
      @RequestParam String street,
      @RequestParam String postalCode,
      HttpSession session,
      RedirectAttributes ra) {
    try {
      Long clientId = (Long) session.getAttribute("userId");
      OrderRequest orderRequest = new OrderRequest();
      orderRequest.setClientId(clientId);
      orderRequest.setProductIds(productIds);
      orderRequest.setCity(city);
      orderRequest.setStreet(street);
      orderRequest.setPostalCode(postalCode);
      orderService.createOrder(orderRequest);
      ra.addFlashAttribute("message", "Order created successfully!");
      return "redirect:/orders"; // returns the orders.html template
    } catch (RuntimeException ex) { // mudar serviços pra lidar com as exceptions
      ra.addFlashAttribute("error", ex.getMessage());
      return "redirect:/restaurants";
    }
  }

  // UC N - cancelar pedido

  // ---------------------

}
