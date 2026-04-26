package pt.ul.fc.css.tascaeats.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import pt.ul.fc.css.tascaeats.dto.order.OrderRequest;
import pt.ul.fc.css.tascaeats.dto.order.PaymentRequest;
import pt.ul.fc.css.tascaeats.entities.Admin;
import pt.ul.fc.css.tascaeats.entities.Client;
import pt.ul.fc.css.tascaeats.entities.Delivery;
import pt.ul.fc.css.tascaeats.entities.DeliveryPerson;
import pt.ul.fc.css.tascaeats.entities.Order;
import pt.ul.fc.css.tascaeats.entities.Product;
import pt.ul.fc.css.tascaeats.entities.Restaurant;
import pt.ul.fc.css.tascaeats.entities.Address;
import pt.ul.fc.css.tascaeats.entities.enums.DeliveryPersonStatus;
import pt.ul.fc.css.tascaeats.entities.enums.OrderStatus;
import pt.ul.fc.css.tascaeats.entities.enums.ProductStatus;
import pt.ul.fc.css.tascaeats.entities.enums.RestaurantStatus;
import pt.ul.fc.css.tascaeats.repository.OrderRepository;
import pt.ul.fc.css.tascaeats.repository.ProductRepository;
import pt.ul.fc.css.tascaeats.repository.RestaurantRepository;
import pt.ul.fc.css.tascaeats.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock private OrderRepository orderRepository;
    @Mock private UserRepository userRepository;
    @Mock private RestaurantRepository restaurantRepository;
    @Mock private ProductRepository productRepository;

    @InjectMocks
    private OrderService orderService;

    // helpers
    private Address address() { return new Address("Rua A", "Lisboa", "1000-001"); }
    private Admin admin() { return new Admin("admin", "pass", "Admin", "912345678", "987654321"); }
    private Restaurant restaurant() { return new Restaurant("123456789", "Cantina", address(), admin()); }
    private Client client() { return new Client("client", "pass", "Client", "912345678", "123456789", List.of(address())); }
    private Product product(Restaurant r) { return new Product(9.99, "Mussarela", "Pizza", r); }

    // --- CREATE ORDER ---

    @Test
    void createOrder_shouldReturnNull_whenClientNotFound() {
        OrderRequest request = new OrderRequest();
        request.setClientId(1L);
        request.setProductIds(List.of(1L));

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertNull(orderService.createOrder(request));
    }

    @Test
    void createOrder_shouldReturnNull_whenUserIsNotClient() {
        OrderRequest request = new OrderRequest();
        request.setClientId(1L);
        request.setProductIds(List.of(1L));

        when(userRepository.findById(1L)).thenReturn(Optional.of(admin()));

        assertNull(orderService.createOrder(request));
    }

    @Test
    void createOrder_shouldReturnNull_whenProductNotFound() {
        OrderRequest request = new OrderRequest();
        request.setClientId(1L);
        request.setProductIds(List.of(1L));

        when(userRepository.findById(1L)).thenReturn(Optional.of(client()));
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertNull(orderService.createOrder(request));
    }

    @Test
    void createOrder_shouldReturnNull_whenRestaurantClosed() {
        Restaurant restaurant = restaurant();
        restaurant.setStatus(RestaurantStatus.CLOSED);
        Product product = product(restaurant);

        OrderRequest request = new OrderRequest();
        request.setClientId(1L);
        request.setProductIds(List.of(1L));

        when(userRepository.findById(1L)).thenReturn(Optional.of(client()));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        assertNull(orderService.createOrder(request));
    }

    @Test
    void createOrder_shouldReturnNull_whenProductSoldOut() {
        Restaurant restaurant = restaurant();
        Product product = product(restaurant);
        product.setStatus(ProductStatus.SOLD_OUT);

        OrderRequest request = new OrderRequest();
        request.setClientId(1L);
        request.setProductIds(List.of(1L));

        when(userRepository.findById(1L)).thenReturn(Optional.of(client()));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        assertNull(orderService.createOrder(request));
    }

    @Test
    void createOrder_shouldReturnOrderResponse_whenSuccessful() {
        Restaurant restaurant = restaurant();
        Product product = product(restaurant);

        OrderRequest request = new OrderRequest();
        request.setClientId(1L);
        request.setProductIds(List.of(1L));

        when(userRepository.findById(1L)).thenReturn(Optional.of(client()));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(orderRepository.save(any(Order.class))).thenAnswer(inv -> inv.getArgument(0));

        assertNotNull(orderService.createOrder(request));
    }

    // --- PROCESS PAYMENT ---

    @Test
    void processPayment_shouldReturnNull_whenOrderMissing() {
        PaymentRequest request = new PaymentRequest();
        request.setOrderId(1L);
        request.setPaymentMethod("CASH");
        request.setAmount(9.99);

        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        assertNull(orderService.processPayment(request));
    }

    @Test
    void processPayment_shouldReturnNull_whenAmountMismatch() {
        Restaurant restaurant = restaurant();
        Address address = address();
        Order order = new Order(client(), List.of(product(restaurant)), address);

        PaymentRequest request = new PaymentRequest();
        request.setOrderId(1L);
        request.setPaymentMethod("CASH");
        request.setAmount(8.99);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        assertNull(orderService.processPayment(request));
    }

    @Test
    void processPayment_shouldReturnNull_whenOrderNotInCreatedStatus() {
        Restaurant restaurant = restaurant();
        Address address = address();
        Order order = new Order(client(), List.of(product(restaurant)), address);
        order.setOrderStatus(OrderStatus.PAID);

        PaymentRequest request = new PaymentRequest();
        request.setOrderId(1L);
        request.setPaymentMethod("CASH");
        request.setAmount(9.99);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        assertNull(orderService.processPayment(request));
    }

    @Test
    void processPayment_shouldReturnOrderResponse_whenSuccessful() {
        Restaurant restaurant = restaurant();
        Address address = address();
        Order order = new Order(client(), List.of(product(restaurant)), address);

        PaymentRequest request = new PaymentRequest();
        request.setOrderId(1L);
        request.setPaymentMethod("CASH");
        request.setAmount(9.99);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenAnswer(inv -> inv.getArgument(0));

        assertEquals(OrderStatus.PAID, orderService.processPayment(request).status());
    }

    // --- UPDATE ORDER STATUS ---

    @Test
    void updateOrderStatus_shouldReturnNull_whenOrderNotFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());
        assertNull(orderService.updateOrderStatus(1L, "PAID"));
    }

    // CREATED → transições inválidas
    @Test
    void updateOrderStatus_shouldReturnNull_whenCreatedToPreparing() {
        Restaurant restaurant = restaurant();
        Address address = address();
        Order order = new Order(client(), List.of(product(restaurant)), address);
        // CREATED → PREPARING é inválido

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        assertNull(orderService.updateOrderStatus(1L, "PREPARING"));
    }

    @Test
    void updateOrderStatus_shouldReturnNull_whenCreatedToDelivered() {
        Restaurant restaurant = restaurant();
        Address address = address();
        Order order = new Order(client(), List.of(product(restaurant)), address);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        assertNull(orderService.updateOrderStatus(1L, "DELIVERED"));
    }

    // PAID → transições inválidas
    @Test
    void updateOrderStatus_shouldReturnNull_whenPaidToReady() {
        Restaurant restaurant = restaurant();
        Address address = address();
        Order order = new Order(client(), List.of(product(restaurant)), address);
        order.setOrderStatus(OrderStatus.PAID);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        assertNull(orderService.updateOrderStatus(1L, "READY"));
    }

    @Test
    void updateOrderStatus_shouldReturnNull_whenPaidToDelivered() {
        Restaurant restaurant = restaurant();
        Address address = address();
        Order order = new Order(client(), List.of(product(restaurant)), address);
        order.setOrderStatus(OrderStatus.PAID);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        assertNull(orderService.updateOrderStatus(1L, "DELIVERED"));
    }

    // PREPARING → transições inválidas
    @Test
    void updateOrderStatus_shouldReturnNull_whenPreparingToPaid() {
        Restaurant restaurant = restaurant();
        Address address = address();
        Order order = new Order(client(), List.of(product(restaurant)), address);
        order.setOrderStatus(OrderStatus.PREPARING);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        assertNull(orderService.updateOrderStatus(1L, "PAID"));
    }

    @Test
    void updateOrderStatus_shouldReturnNull_whenPreparingToDelivered() {
        Restaurant restaurant = restaurant();
        Address address = address();
        Order order = new Order(client(), List.of(product(restaurant)), address);
        order.setOrderStatus(OrderStatus.PREPARING);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        assertNull(orderService.updateOrderStatus(1L, "DELIVERED"));
    }

    // READY → transições inválidas
    @Test
    void updateOrderStatus_shouldReturnNull_whenReadyToPaid() {
        Restaurant restaurant = restaurant();
        Address address = address();
        Order order = new Order(client(), List.of(product(restaurant)), address);
        order.setOrderStatus(OrderStatus.READY);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        assertNull(orderService.updateOrderStatus(1L, "PAID"));
    }

    @Test
    void updateOrderStatus_shouldReturnNull_whenReadyToPreparing() {
        Restaurant restaurant = restaurant();
        Address address = address();
        Order order = new Order(client(), List.of(product(restaurant)), address);
        order.setOrderStatus(OrderStatus.READY);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        assertNull(orderService.updateOrderStatus(1L, "PREPARING"));
    }

    @Test
    void updateOrderStatus_shouldReturnNull_whenAlreadyDelivered() {
        Restaurant restaurant = restaurant();
        Address address = address();
        Order order = new Order(client(), List.of(product(restaurant)), address);
        order.setOrderStatus(OrderStatus.DELIVERED);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        assertNull(orderService.updateOrderStatus(1L, "READY"));
    }

    @Test
    void updateOrderStatus_shouldReturnOrderResponse_whenValidTransition() {
        Restaurant restaurant = restaurant();
        Address address = address();
        Order order = new Order(client(), List.of(product(restaurant)), address);
        // CREATED → PAID is valid
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenAnswer(inv -> inv.getArgument(0));

        assertNotNull(orderService.updateOrderStatus(1L, "PAID"));
    }

    @Test
    void updateOrderStatus_shouldReturnNull_whenDeliveredButNoDeliveryAssigned() {
        Restaurant restaurant = restaurant();
        Address address = address();
        Order order = new Order(client(), List.of(product(restaurant)), address);
        order.setOrderStatus(OrderStatus.READY);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        assertNull(orderService.updateOrderStatus(1L, "DELIVERED"));
    }

    // Status terminal → qualquer transição é inválida
    @Test
    void updateOrderStatus_shouldReturnNull_whenAlreadyCancelled() {
        Restaurant restaurant = restaurant();
        Address address = address();
        Order order = new Order(client(), List.of(product(restaurant)), address);
        order.setOrderStatus(OrderStatus.CANCELLED);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        assertNull(orderService.updateOrderStatus(1L, "CREATED"));
    }

    // Prepared -> Delivered
    @Test
    void updateOrderStatus_shouldReturnOrderResponse_whenReadyToDeliveredWithDelivery() {
        Restaurant restaurant = restaurant();
        Address address = address();
        Order order = new Order(client(), List.of(product(restaurant)), address);
        order.setOrderStatus(OrderStatus.READY);

        DeliveryPerson delivery = new DeliveryPerson("delivery", "pass", "Delivery", "931234567", "111222333");
        delivery.setDeliveryPersonStatus(DeliveryPersonStatus.BUSY);
        order.setDeliveryPerson(delivery);
        order.setDelivery(new Delivery(LocalDateTime.now()));

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenAnswer(inv -> inv.getArgument(0));

        assertNotNull(orderService.updateOrderStatus(1L, "DELIVERED"));
        assertEquals(DeliveryPersonStatus.AVAILABLE, delivery.getDeliveryPersonStatus());
    }

    // --- ASSIGN DELIVERY PERSON ---

    @Test
    void assignDeliveryPerson_shouldReturnNull_whenOrderNotFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());
        assertNull(orderService.assignDeliveryPerson(1L, 2L));
    }

    @Test
    void assignDeliveryPerson_shouldReturnNull_whenDeliveryPersonNotFound() {
        Restaurant restaurant = restaurant();
        Address address = address();
        Order order = new Order(client(), List.of(product(restaurant)), address);
        order.setOrderStatus(OrderStatus.READY);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        assertNull(orderService.assignDeliveryPerson(1L, 2L));
    }

    @Test
    void assignDeliveryPerson_shouldReturnNull_whenDeliveryPersonBusy() {
        Restaurant restaurant = restaurant();
        Address address = address();
        Order order = new Order(client(), List.of(product(restaurant)), address);
        order.setOrderStatus(OrderStatus.READY);

        DeliveryPerson delivery = new DeliveryPerson("delivery", "pass", "Delivery", "931234567", "111222333");
        delivery.setDeliveryPersonStatus(DeliveryPersonStatus.BUSY);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(userRepository.findById(2L)).thenReturn(Optional.of(delivery));

        assertNull(orderService.assignDeliveryPerson(1L, 2L));
    }

    @Test
    void assignDeliveryPerson_shouldReturnNull_whenOrderNotReady() {
        Restaurant restaurant = restaurant();
        Address address = address();
        Order order = new Order(client(), List.of(product(restaurant)), address);
        // status is CREATED, not READY

        DeliveryPerson delivery = new DeliveryPerson("delivery", "pass", "Delivery", "931234567", "111222333");
        delivery.setDeliveryPersonStatus(DeliveryPersonStatus.AVAILABLE);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(userRepository.findById(2L)).thenReturn(Optional.of(delivery));

        assertNull(orderService.assignDeliveryPerson(1L, 2L));
    }

    @Test
    void assignDeliveryPerson_shouldReturnOrderResponse_whenSuccessful() {
        Restaurant restaurant = restaurant();
        Address address = address();
        Order order = new Order(client(), List.of(product(restaurant)), address);
        order.setOrderStatus(OrderStatus.READY);

        DeliveryPerson delivery = new DeliveryPerson("delivery", "pass", "Delivery", "931234567", "111222333");
        delivery.setDeliveryPersonStatus(DeliveryPersonStatus.AVAILABLE);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(userRepository.findById(2L)).thenReturn(Optional.of(delivery));
        when(orderRepository.save(any(Order.class))).thenAnswer(inv -> inv.getArgument(0));

        assertNotNull(orderService.assignDeliveryPerson(1L, 2L));
    }

    // --- CANCEL ORDER ---

    @Test
    void cancelOrder_shouldReturnTrue_whenOrderDoesNotExist() {
        when(orderRepository.existsById(1L)).thenReturn(false);
        assertTrue(orderService.cancelOrder(1L));
    }

    @Test
    void cancelOrder_shouldReturnFalse_whenOrderNotCancellable() {
        Restaurant restaurant = restaurant();
        Address address = address();
        Order order = new Order(client(), List.of(product(restaurant)), address);
        order.setOrderStatus(OrderStatus.PREPARING);

        when(orderRepository.existsById(1L)).thenReturn(true);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        assertFalse(orderService.cancelOrder(1L));
    }

    @Test
    void cancelOrder_shouldReturnTrue_whenOrderInCreatedStatus() {
        Restaurant restaurant = restaurant();
        Address address = address();
        Order order = new Order(client(), List.of(product(restaurant)), address);
        // status is CREATED by default

        when(orderRepository.existsById(1L)).thenReturn(true);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        assertTrue(orderService.cancelOrder(1L));
    }

    @Test
    void cancelOrder_shouldReturnTrue_whenOrderInPaidStatus() {
        Restaurant restaurant = restaurant();
        Address address = address();
        Order order = new Order(client(), List.of(product(restaurant)), address);
        order.setOrderStatus(OrderStatus.PAID);

        when(orderRepository.existsById(1L)).thenReturn(true);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        assertTrue(orderService.cancelOrder(1L));
    }
}