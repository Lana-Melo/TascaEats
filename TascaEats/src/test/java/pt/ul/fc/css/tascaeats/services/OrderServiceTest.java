package pt.ul.fc.css.tascaeats.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pt.ul.fc.css.tascaeats.dto.order.OrderRequestDto;
import pt.ul.fc.css.tascaeats.dto.order.OrderResponseDto;
import pt.ul.fc.css.tascaeats.dto.order.PaymentRequestDto;
import pt.ul.fc.css.tascaeats.entities.Address;
import pt.ul.fc.css.tascaeats.entities.Admin;
import pt.ul.fc.css.tascaeats.entities.Client;
import pt.ul.fc.css.tascaeats.entities.DeliveryPerson;
import pt.ul.fc.css.tascaeats.entities.Menu;
import pt.ul.fc.css.tascaeats.entities.Order;
import pt.ul.fc.css.tascaeats.entities.Product;
import pt.ul.fc.css.tascaeats.entities.ProductItem;
import pt.ul.fc.css.tascaeats.entities.Restaurant;
import pt.ul.fc.css.tascaeats.entities.enums.DeliveryPersonStatus;
import pt.ul.fc.css.tascaeats.entities.enums.OrderStatus;
import pt.ul.fc.css.tascaeats.entities.enums.ProductStatus;
import pt.ul.fc.css.tascaeats.entities.enums.RestaurantStatus;
import pt.ul.fc.css.tascaeats.exceptions.IncompleteInputException;
import pt.ul.fc.css.tascaeats.exceptions.RequirementsNotMetException;
import pt.ul.fc.css.tascaeats.exceptions.ResourceNotFoundException;
import pt.ul.fc.css.tascaeats.repository.OrderRepository;
import pt.ul.fc.css.tascaeats.repository.ProductItemRepository;
import pt.ul.fc.css.tascaeats.repository.RestaurantRepository;
import pt.ul.fc.css.tascaeats.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

  @Mock private OrderRepository orderRepository;
  @Mock private UserRepository userRepository;
  @Mock private RestaurantRepository restaurantRepository;
  @Mock private ProductItemRepository productItemRepository;

  @InjectMocks private OrderService orderService;

  private Address address() {
    return new Address("Rua A", "Lisboa", "1000-001");
  }

  private Admin admin() {
    return new Admin("admin", "pass", "Admin", "912345678", "987654321");
  }

  private Restaurant restaurant() {
    return new Restaurant(
        "123456789", "Cantina", address(), admin(), LocalTime.of(11, 0), LocalTime.of(22, 0));
  }

  private Client client() {
    return new Client("client", "pass", "Client", "912345678", "123456789", List.of(address()));
  }

  private ProductItem productItem(Restaurant r) {
    Menu m = new Menu("menu", "italian", "desc");
    Product p = new Product(9.99, "Mussarela", "Pizza", m);
    return new ProductItem(p, r);
  }

  private Order buildOrder(Client c, ProductItem pi) {
    return new Order(c, List.of(pi), address());
  }

  // ─── createOrder ────────────────────────────────────────────────────────────

  @Test
  void createOrder_shouldThrow_whenClientNotFound() {
    OrderRequestDto req = new OrderRequestDto();
    req.setClientId(1L);
    req.setProductIds(List.of(1L));
    req.setCity("Lisboa");
    req.setStreet("Rua A");
    req.setPostalCode("1000-001");
    when(userRepository.findById(1L)).thenReturn(Optional.empty());
    assertThrows(ResourceNotFoundException.class, () -> orderService.createOrder(req));
  }

  @Test
  void createOrder_shouldThrow_whenUserIsNotClient() {
    OrderRequestDto req = new OrderRequestDto();
    req.setClientId(1L);
    req.setProductIds(List.of(1L));
    req.setCity("Lisboa");
    req.setStreet("Rua A");
    req.setPostalCode("1000-001");
    when(userRepository.findById(1L)).thenReturn(Optional.of(admin()));
    assertThrows(ResourceNotFoundException.class, () -> orderService.createOrder(req));
  }

  @Test
  void createOrder_shouldThrow_whenProductNotFound() {
    OrderRequestDto req = new OrderRequestDto();
    req.setClientId(1L);
    req.setProductIds(List.of(1L));
    req.setCity("Lisboa");
    req.setStreet("Rua A");
    req.setPostalCode("1000-001");
    when(userRepository.findById(1L)).thenReturn(Optional.of(client()));
    when(productItemRepository.findById(1L)).thenReturn(Optional.empty());
    assertThrows(ResourceNotFoundException.class, () -> orderService.createOrder(req));
  }

  @Test
  void createOrder_shouldThrow_whenProductSoldOut() {
    Restaurant r = restaurant();
    ProductItem pi = productItem(r);
    pi.setStatus(ProductStatus.SOLD_OUT);
    OrderRequestDto req = new OrderRequestDto();
    req.setClientId(1L);
    req.setProductIds(List.of(1L));
    req.setCity("Lisboa");
    req.setStreet("Rua A");
    req.setPostalCode("1000-001");
    when(userRepository.findById(1L)).thenReturn(Optional.of(client()));
    when(productItemRepository.findById(1L)).thenReturn(Optional.of(pi));
    assertThrows(ResourceNotFoundException.class, () -> orderService.createOrder(req));
  }

  @Test
  void createOrder_shouldThrow_whenRestaurantClosed() {
    Restaurant r = restaurant();
    r.setStatus(RestaurantStatus.CLOSED);
    ProductItem pi = productItem(r);
    OrderRequestDto req = new OrderRequestDto();
    req.setClientId(1L);
    req.setProductIds(List.of(1L));
    req.setCity("Lisboa");
    req.setStreet("Rua A");
    req.setPostalCode("1000-001");
    when(userRepository.findById(1L)).thenReturn(Optional.of(client()));
    when(productItemRepository.findById(1L)).thenReturn(Optional.of(pi));
    assertThrows(ResourceNotFoundException.class, () -> orderService.createOrder(req));
  }

  @Test
  void createOrder_shouldThrow_whenAddressMissing() {
    OrderRequestDto req = new OrderRequestDto();
    req.setClientId(1L);
    req.setProductIds(List.of(1L));
    assertThrows(IncompleteInputException.class, () -> orderService.createOrder(req));
  }

  @Test
  void createOrder_shouldReturnOrder_whenValid() {
    Restaurant r = restaurant();
    ProductItem pi = productItem(r);
    Client c = client();
    Order order = buildOrder(c, pi);
    OrderRequestDto req = new OrderRequestDto();
    req.setClientId(1L);
    req.setProductIds(List.of(1L));
    req.setCity("Lisboa");
    req.setStreet("Rua A");
    req.setPostalCode("1000-001");
    when(userRepository.findById(1L)).thenReturn(Optional.of(c));
    when(productItemRepository.findById(1L)).thenReturn(Optional.of(pi));
    when(orderRepository.save(any(Order.class))).thenReturn(order);
    when(restaurantRepository.findById(any())).thenReturn(Optional.of(r));
    when(restaurantRepository.save(any())).thenReturn(r);
    OrderResponseDto result = orderService.createOrder(req);
    assertNotNull(result);
  }

  // ─── processPayment ─────────────────────────────────────────────────────────

  @Test
  void processPayment_shouldThrow_whenOrderNotFound() {
    PaymentRequestDto req = new PaymentRequestDto();
    req.setOrderId(99L);
    req.setPaymentMethod("Cash");
    req.setAmount(10.0);
    when(orderRepository.findById(99L)).thenReturn(Optional.empty());
    assertThrows(ResourceNotFoundException.class, () -> orderService.processPayment(req));
  }

  @Test
  void processPayment_shouldThrow_whenOrderAlreadyPaid() {
    Restaurant r = restaurant();
    Order order = buildOrder(client(), productItem(r));
    order.setOrderStatus(OrderStatus.PAID);
    PaymentRequestDto req = new PaymentRequestDto();
    req.setOrderId(1L);
    req.setPaymentMethod("Cash");
    req.setAmount(10.0);
    when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
    assertThrows(RequirementsNotMetException.class, () -> orderService.processPayment(req));
  }

  @Test
  void processPayment_shouldThrow_whenCashAmountInsufficient() {
    Restaurant r = restaurant();
    Order order = buildOrder(client(), productItem(r)); // totalValue = 9.99
    PaymentRequestDto req = new PaymentRequestDto();
    req.setOrderId(1L);
    req.setPaymentMethod("Cash");
    req.setAmount(5.0);
    when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
    assertThrows(RequirementsNotMetException.class, () -> orderService.processPayment(req));
  }

  @Test
  void processPayment_shouldReturnPaidOrder_withCash() {
    Restaurant r = restaurant();
    Order order = buildOrder(client(), productItem(r)); // totalValue = 9.99
    PaymentRequestDto req = new PaymentRequestDto();
    req.setOrderId(1L);
    req.setPaymentMethod("Cash");
    req.setAmount(10.0);
    req.setPaymentInfo(null);
    when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
    when(orderRepository.save(any(Order.class))).thenReturn(order);
    assertNotNull(orderService.processPayment(req));
  }

  @Test
  void processPayment_shouldThrow_whenMBWayAmountMismatch() {
    Restaurant r = restaurant();
    Order order = buildOrder(client(), productItem(r)); // totalValue = 9.99
    PaymentRequestDto req = new PaymentRequestDto();
    req.setOrderId(1L);
    req.setPaymentMethod("MBWay");
    req.setAmount(5.0);
    req.setPaymentInfo("912345678");
    when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
    assertThrows(RequirementsNotMetException.class, () -> orderService.processPayment(req));
  }

  @Test
  void processPayment_shouldReturnPaidOrder_withCard() {
    Restaurant r = restaurant();
    Order order = buildOrder(client(), productItem(r)); // totalValue = 9.99
    PaymentRequestDto req = new PaymentRequestDto();
    req.setOrderId(1L);
    req.setPaymentMethod("Card");
    req.setAmount(9.99);
    req.setPaymentInfo("4111111111111111");
    when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
    when(orderRepository.save(any(Order.class))).thenReturn(order);
    assertNotNull(orderService.processPayment(req));
  }

  @Test
  void processPayment_shouldReturnPaidOrder_withMBWay() {
    Restaurant r = restaurant();
    Order order = buildOrder(client(), productItem(r)); // totalValue = 9.99
    PaymentRequestDto req = new PaymentRequestDto();
    req.setOrderId(1L);
    req.setPaymentMethod("MBWay");
    req.setAmount(9.99);
    req.setPaymentInfo("912345678");
    when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
    when(orderRepository.save(any(Order.class))).thenReturn(order);
    assertNotNull(orderService.processPayment(req));
  }

  @Test
  void processPayment_shouldThrow_whenInvalidMethod() {
    Restaurant r = restaurant();
    Order order = buildOrder(client(), productItem(r));
    PaymentRequestDto req = new PaymentRequestDto();
    req.setOrderId(1L);
    req.setPaymentMethod("Crypto");
    req.setAmount(9.99);
    when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
    assertThrows(IllegalArgumentException.class, () -> orderService.processPayment(req));
  }

  // ─── updateOrderStatus ──────────────────────────────────────────────────────

  @Test
  void updateOrderStatus_shouldThrow_whenOrderNotFound() {
    when(orderRepository.findById(99L)).thenReturn(Optional.empty());
    assertThrows(
        ResourceNotFoundException.class, () -> orderService.updateOrderStatus(99L, "PAID"));
  }

  @Test
  void updateOrderStatus_shouldThrow_whenCreatedToInvalidStatus() {
    Order order = buildOrder(client(), productItem(restaurant())); // CREATED
    when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
    assertThrows(
        RequirementsNotMetException.class, () -> orderService.updateOrderStatus(1L, "DELIVERED"));
  }

  @Test
  void updateOrderStatus_shouldSucceed_fromCreatedToPaid() {
    Order order = buildOrder(client(), productItem(restaurant()));
    when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
    when(orderRepository.save(any(Order.class))).thenReturn(order);
    assertNotNull(orderService.updateOrderStatus(1L, "PAID"));
  }

  @Test
  void updateOrderStatus_shouldSucceed_fromCreatedToCancelled() {
    Order order = buildOrder(client(), productItem(restaurant()));
    when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
    when(orderRepository.save(any(Order.class))).thenReturn(order);
    assertNotNull(orderService.updateOrderStatus(1L, "CANCELLED"));
  }

  @Test
  void updateOrderStatus_shouldThrow_whenReadyToInvalidStatus() {
    Order order = buildOrder(client(), productItem(restaurant()));
    order.setOrderStatus(OrderStatus.READY);
    when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
    assertThrows(
        RequirementsNotMetException.class, () -> orderService.updateOrderStatus(1L, "CANCELLED"));
  }

  @Test
  void updateOrderStatus_shouldAssignDelivery_whenPreparingAndDeliveryAvailable() {
    Order order = buildOrder(client(), productItem(restaurant()));
    order.setOrderStatus(OrderStatus.PREPARING);
    DeliveryPerson del = new DeliveryPerson("dp", "pass", "DP", "912000000", "111111111");
    del.setDeliveryPersonStatus(DeliveryPersonStatus.AVAILABLE);
    when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
    when(userRepository.findFirstByStatus(DeliveryPersonStatus.AVAILABLE))
        .thenReturn(Optional.of(del));
    when(userRepository.save(any())).thenReturn(del);
    when(orderRepository.save(any(Order.class))).thenReturn(order);

    OrderResponseDto result = orderService.updateOrderStatus(1L, "READY");

    assertNotNull(result);
    assertEquals(OrderStatus.READY, result.status());
  }

  @Test
  void updateOrderStatus_shouldReturnDelivered_whenReadyAndDeliveryExists() {
    Order order = buildOrder(client(), productItem(restaurant()));
    order.setOrderStatus(OrderStatus.READY);
    DeliveryPerson del = new DeliveryPerson("dp", "pass", "DP", "912000000", "111111111");
    del.setDeliveryPersonStatus(DeliveryPersonStatus.BUSY);
    order.setDeliveryPerson(del);
    when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
    when(orderRepository.save(any(Order.class))).thenReturn(order);

    OrderResponseDto result = orderService.updateOrderStatus(1L, "DELIVERED");

    assertNotNull(result);
    assertEquals(OrderStatus.DELIVERED, result.status());
  }

  @Test
  void updateOrderStatus_shouldReturnReady_whenPreparingAndNoDeliveryAvailable() {
    Order order = buildOrder(client(), productItem(restaurant()));
    order.setOrderStatus(OrderStatus.PREPARING);
    when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
    when(userRepository.findFirstByStatus(DeliveryPersonStatus.AVAILABLE))
        .thenReturn(Optional.empty());
    when(orderRepository.save(any(Order.class))).thenReturn(order);

    OrderResponseDto result = orderService.updateOrderStatus(1L, "READY");

    assertNotNull(result);
    assertEquals(OrderStatus.READY, result.status());
  }

  @Test
  void updateOrderStatus_shouldThrow_whenDeliveredWithoutDelivery() {
    Order order = buildOrder(client(), productItem(restaurant()));
    order.setOrderStatus(OrderStatus.READY);
    when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

    assertThrows(
        RequirementsNotMetException.class, () -> orderService.updateOrderStatus(1L, "DELIVERED"));
  }

  @Test
  void getOrdersByClient_shouldReturnList() {
    Order order = buildOrder(client(), productItem(restaurant()));
    when(orderRepository.findByClientId(1L)).thenReturn(List.of(order));

    var result = orderService.getOrdersByClient(1L);

    assertEquals(1, result.size());
  }

  @Test
  void searchByStatus_shouldReturnList_whenStatusMatches() {
    Order order = buildOrder(client(), productItem(restaurant()));
    order.setOrderStatus(OrderStatus.PAID);
    when(orderRepository.findAll()).thenReturn(List.of(order));

    var result = orderService.searchByStatus("paid");

    assertEquals(1, result.size());
  }

  @Test
  void searchByStatus_shouldReturnAll_whenStatusNull() {
    Order order = buildOrder(client(), productItem(restaurant()));
    when(orderRepository.findAll()).thenReturn(List.of(order));

    var result = orderService.searchByStatus(null);

    assertEquals(1, result.size());
  }

  // ─── assignDeliveryPerson ───────────────────────────────────────────────────

  @Test
  void assignDeliveryPerson_shouldThrow_whenOrderNotFound() {
    when(orderRepository.findById(99L)).thenReturn(Optional.empty());
    assertThrows(ResourceNotFoundException.class, () -> orderService.assignDeliveryPerson(99L, 1L));
  }

  @Test
  void assignDeliveryPerson_shouldThrow_whenUserNotDeliveryPerson() {
    Order order = buildOrder(client(), productItem(restaurant()));
    order.setOrderStatus(OrderStatus.READY);
    when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
    when(userRepository.findById(1L)).thenReturn(Optional.of(client()));
    assertThrows(ResourceNotFoundException.class, () -> orderService.assignDeliveryPerson(1L, 1L));
  }

  @Test
  void assignDeliveryPerson_shouldThrow_whenDeliveryPersonBusy() {
    Order order = buildOrder(client(), productItem(restaurant()));
    order.setOrderStatus(OrderStatus.READY);
    DeliveryPerson dp = new DeliveryPerson("dp", "pass", "DP", "912000000", "111111111");
    dp.setDeliveryPersonStatus(DeliveryPersonStatus.BUSY);
    when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
    when(userRepository.findById(1L)).thenReturn(Optional.of(dp));
    assertThrows(
        RequirementsNotMetException.class, () -> orderService.assignDeliveryPerson(1L, 1L));
  }

  @Test
  void assignDeliveryPerson_shouldThrow_whenOrderNotReady() {
    Order order = buildOrder(client(), productItem(restaurant())); // CREATED
    DeliveryPerson dp = new DeliveryPerson("dp", "pass", "DP", "912000000", "111111111");
    when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
    when(userRepository.findById(1L)).thenReturn(Optional.of(dp));
    assertThrows(
        RequirementsNotMetException.class, () -> orderService.assignDeliveryPerson(1L, 1L));
  }

  @Test
  void assignDeliveryPerson_shouldSucceed_whenValid() {
    Order order = buildOrder(client(), productItem(restaurant()));
    order.setOrderStatus(OrderStatus.READY);
    DeliveryPerson dp = new DeliveryPerson("dp", "pass", "DP", "912000000", "111111111");
    when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
    when(userRepository.findById(1L)).thenReturn(Optional.of(dp));
    when(userRepository.save(any())).thenReturn(dp);
    when(orderRepository.save(any(Order.class))).thenReturn(order);
    assertNotNull(orderService.assignDeliveryPerson(1L, 1L));
  }

  // ─── cancelOrder ────────────────────────────────────────────────────────────

  @Test
  void cancelOrder_shouldReturnFalse_whenNotFound() {
    when(orderRepository.existsById(99L)).thenReturn(false);
    assertFalse(orderService.cancelOrder(99L));
  }

  @Test
  void cancelOrder_shouldReturnFalse_whenOrderNotCancellable() {
    Order order = buildOrder(client(), productItem(restaurant()));
    order.setOrderStatus(OrderStatus.PREPARING);
    when(orderRepository.existsById(1L)).thenReturn(true);
    when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
    assertFalse(orderService.cancelOrder(1L));
  }

  @Test
  void cancelOrder_shouldReturnTrue_whenOrderCreated() {
    Order order = buildOrder(client(), productItem(restaurant()));
    when(orderRepository.existsById(1L)).thenReturn(true);
    when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
    assertTrue(orderService.cancelOrder(1L));
    verify(orderRepository).deleteById(1L);
  }

  @Test
  void cancelOrder_shouldReturnTrue_whenOrderPaid() {
    Order order = buildOrder(client(), productItem(restaurant()));
    order.setOrderStatus(OrderStatus.PAID);
    when(orderRepository.existsById(1L)).thenReturn(true);
    when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
    assertTrue(orderService.cancelOrder(1L));
    verify(orderRepository).deleteById(1L);
  }

  // ─── getAllOrders ────────────────────────────────────────────────────────────

  @Test
  void getAllOrders_shouldReturnList() {
    Order order = buildOrder(client(), productItem(restaurant()));
    when(orderRepository.findAll()).thenReturn(List.of(order));
    assertEquals(1, orderService.getAllOrders().size());
  }

  @Test
  void getAllOrders_shouldReturnEmptyList_whenNoOrders() {
    when(orderRepository.findAll()).thenReturn(List.of());
    assertTrue(orderService.getAllOrders().isEmpty());
  }
}
