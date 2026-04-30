package pt.ul.fc.css.tascaeats.controller;

import jakarta.servlet.http.HttpSession;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pt.ul.fc.css.tascaeats.dto.order.OrderRequestDto;
import pt.ul.fc.css.tascaeats.dto.users.request.ClientRequestDto;
import pt.ul.fc.css.tascaeats.dto.users.response.UserResponseDto;
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
      UserResponseDto userDto =
          userService.login(
              username,
              password); // apesar de ser um cliente, o loginn retorna UserRequest mas n faz mal n
      // precisamos da morada
      session.setAttribute("userId", userDto.id());
      // session.setAttribute("userType", userDto.userType()); //nao sei, talvez seja preciso
      ra.addFlashAttribute("message", "Login successful!");
      return "redirect:/restaurant"; // quem fizer login pela interface web vai sempre para os
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
      @RequestParam String city,
      @RequestParam String postalCode,
      @RequestParam String street,
      Model model,
      RedirectAttributes ra) {
    try {
      ClientRequestDto clientRequest = new ClientRequestDto();
      clientRequest.setUsername(username);
      clientRequest.setPassword(password);
      clientRequest.setName(name);
      clientRequest.setPhoneNumber(phoneNumber);
      clientRequest.setNif(nif);
      clientRequest.setCity(city);
      clientRequest.setPostalCode(postalCode);
      clientRequest.setStreet(street);
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
  @GetMapping("/restaurant")
  public String restaurants(
      @RequestParam(required = false) String designation,
      @RequestParam(required = false) String city,
      @RequestParam(required = false) String street,
      @RequestParam(required = false) String postalCode,
      @RequestParam(required = false) String cuisine,
      @RequestParam(required = false) String orderNumber,
      @RequestParam(required = false) String reviewNumber,
      @RequestParam(required = false) String activeHour,
      @RequestParam(required = false) String average,
      HttpSession session,
      Model model) {
    if (session.getAttribute("userId") == null) return "redirect:/login";
    try {
      model.addAttribute("designation", designation);
      model.addAttribute("city", city);
      model.addAttribute("cuisine", cuisine);
      model.addAttribute("orderNumber", orderNumber);
      model.addAttribute("reviewNumber", reviewNumber);
      model.addAttribute("activeHour", activeHour);
      model.addAttribute("average", average);
      model.addAttribute(
          "restaurant",
          restaurantService.searchBy(
              designation,
              city,
              street,
              postalCode,
              cuisine,
              orderNumber,
              reviewNumber,
              activeHour,
              average));
      return "restaurant";
    } catch (RuntimeException ex) {
      model.addAttribute("error", ex.getMessage());
      return "restaurant";
    }
  }

  // UC I - buscar produtos de um restaurante
  @GetMapping("/restaurant/{restaurantId}/product")
  public String restaurantProducts(
      @PathVariable Long restaurantId,
      @RequestParam(required = false) String designation,
      @RequestParam(required = false) String description,
      @RequestParam(required = false) String availability,
      @RequestParam(required = false) String timesOrdered,
      Model model,
      RedirectAttributes ra) {

    try {
      model.addAttribute("restaurantId", restaurantId);
      model.addAttribute("designation", designation);
      model.addAttribute("description", description);
      model.addAttribute("availability", availability);
      model.addAttribute("timesOrdered", timesOrdered);

      model.addAttribute(
          "products",
          restaurantService.searchProductBy(
              restaurantId, designation, description, availability, timesOrdered));

      return "product";

    } catch (RuntimeException ex) {
      ra.addFlashAttribute("error", ex.getMessage());
      return "redirect:/restaurant";
    }
  }

  // ---------------------

  // ------- ORDER -------

  @GetMapping("/order")
  public String orderPage(
      @RequestParam(required = false) String designation,
      @RequestParam(required = false) String description,
      @RequestParam(required = false) String availability,
      @RequestParam(required = false) String timesOrdered,
      @RequestParam(required = false) String newOrder,
      HttpSession session,
      Model model) {
    if (session.getAttribute("userId") == null) return "redirect:/login";
    Long clientId = (Long) session.getAttribute("userId");
    try {
      model.addAttribute("orders", orderService.getOrdersByClient(clientId));
      model.addAttribute("newOrder", newOrder);
      model.addAttribute("designation", designation);
      model.addAttribute("description", description);
      model.addAttribute("availability", availability);
      model.addAttribute("timesOrdered", timesOrdered);
      model.addAttribute(
          "products",
          restaurantService.searchProductBy(
              null, designation, description, availability, timesOrdered));
      return "order";
    } catch (RuntimeException ex) {
      model.addAttribute("error", ex.getMessage());
      return "order";
    }
  }

  // UC J - criar pedido
  @PostMapping("/order")
  public String createOrder(
      @RequestParam List<Long> productIds,
      @RequestParam String city,
      @RequestParam String street,
      @RequestParam String postalCode,
      HttpSession session,
      RedirectAttributes ra) {
    try {
      Long clientId = (Long) session.getAttribute("userId");
      OrderRequestDto orderRequest = new OrderRequestDto();
      orderRequest.setClientId(clientId);
      orderRequest.setProductIds(productIds);
      orderRequest.setCity(city);
      orderRequest.setStreet(street);
      orderRequest.setPostalCode(postalCode);
      orderService.createOrder(orderRequest);
      ra.addFlashAttribute("message", "Order created successfully!");
      return "redirect:/order"; // returns the orders.html template
    } catch (RuntimeException ex) { // mudar serviços pra lidar com as exceptions
      ra.addFlashAttribute("error", ex.getMessage());
      return "redirect:/restaurant";
    }
  }

  // UC N - cancelar pedido

  // ---------------------

}
