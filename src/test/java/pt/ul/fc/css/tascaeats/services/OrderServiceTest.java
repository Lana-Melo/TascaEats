package pt.ul.fc.css.tascaeats.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;

import static org.mockito.Mockito.when;


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
import pt.ul.fc.css.tascaeats.entities.DeliveryPerson;
import pt.ul.fc.css.tascaeats.entities.Order;
import pt.ul.fc.css.tascaeats.entities.Product;
import pt.ul.fc.css.tascaeats.entities.Restaurant;
import pt.ul.fc.css.tascaeats.entities.Address;
import pt.ul.fc.css.tascaeats.entities.enums.DeliveryPersonStatus;
import pt.ul.fc.css.tascaeats.entities.enums.OrderStatus;
import pt.ul.fc.css.tascaeats.repository.OrderRepository;
import pt.ul.fc.css.tascaeats.repository.ProductRepository;
import pt.ul.fc.css.tascaeats.repository.RestaurantRepository;
import pt.ul.fc.css.tascaeats.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private OrderService orderService;

    @Test
    void createOrder_shouldReturnNull_whenRestaurantNotFound() {
        OrderRequest request = new OrderRequest();
        request.setClientId(1L);
        request.setRestaurantId(1L);
        request.setProductIds(List.of(1L));

        when(restaurantRepository.findById(1L)).thenReturn(Optional.empty());

        assertNull(orderService.createOrder(request));
    }

    @Test
    void createOrder_shouldReturnNull_whenProductNotFound() {
        Address address = new Address("Rua A", "Lisboa", "1000-001");
        Admin admin = new Admin("admin", "pass", "Admin", "912345678", "987654321");
        Restaurant restaurant = new Restaurant("123456789", "Cantina", address, admin);
        Client client = new Client("client", "pass", "Client", "912345678", "123456789", new Address("Rua A", "Lisboa", "1000-001"));

        OrderRequest request = new OrderRequest();
        request.setClientId(1L);
        request.setRestaurantId(1L);
        request.setProductIds(List.of(1L));

        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
        when(userRepository.findById(1L)).thenReturn(Optional.of(client));
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertNull(orderService.createOrder(request));
    }

    @Test
    void createOrder_shouldReturnOrderResponse_whenSuccessful() {
        Address address = new Address("Rua A", "Lisboa", "1000-001");
        Admin admin = new Admin("admin", "pass", "Admin", "912345678", "987654321");
        Restaurant restaurant = new Restaurant("123456789", "Cantina", address, admin);
        Client client = new Client("client", "pass", "Client", "912345678", "123456789", new Address("Rua A", "Lisboa", "1000-001"));
        Product product = new Product(9.99, "Mussarela", "Pizza", restaurant);

        OrderRequest request = new OrderRequest();
        request.setClientId(1L);
        request.setRestaurantId(1L);
        request.setProductIds(List.of(1L));

        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
        when(userRepository.findById(1L)).thenReturn(Optional.of(client));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        assertEquals("Cantina", orderService.createOrder(request).restaurantDesignation());
    }

    @Test
    void processPayment_shouldReturnNull_whenOrderMissing() {
        PaymentRequest request = new PaymentRequest();
        request.setOrderId(1L);
        request.setPaymentMethod("CARD");
        request.setAmount(9.99);

        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        assertNull(orderService.processPayment(request));
    }

    @Test
    void processPayment_shouldReturnNull_whenAmountMismatch() {
        Address address = new Address("Rua A", "Lisboa", "1000-001");
        Admin admin = new Admin("admin", "pass", "Admin", "912345678", "987654321");
        Restaurant restaurant = new Restaurant("123456789", "Cantina", address, admin);
        Client client = new Client("client", "pass", "Client", "912345678", "123456789", new Address("Rua A", "Lisboa", "1000-001"));
        Product product = new Product(9.99, "Mussarela", "Pizza", restaurant);
        Order order = new Order(restaurant, client, List.of(product));

        PaymentRequest request = new PaymentRequest();
        request.setOrderId(1L);
        request.setPaymentMethod("CARD");
        request.setAmount(8.99);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        assertNull(orderService.processPayment(request));
    }

    @Test
    void processPayment_shouldReturnOrderResponse_whenSuccessful() {
        Address address = new Address("Rua A", "Lisboa", "1000-001");
        Admin admin = new Admin("admin", "pass", "Admin", "912345678", "987654321");
        Restaurant restaurant = new Restaurant("123456789", "Cantina", address, admin);
        Client client = new Client("client", "pass", "Client", "912345678", "123456789", new Address("Rua A", "Lisboa", "1000-001"));
        Product product = new Product(9.99, "Mussarela", "Pizza", restaurant);
        Order order = new Order(restaurant, client, List.of(product));

        PaymentRequest request = new PaymentRequest();
        request.setOrderId(1L);
        request.setPaymentMethod("CARD");
        request.setAmount(9.99);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        assertEquals(OrderStatus.PAID, orderService.processPayment(request).status());
    }

    @Test
    void updateOrderStatus_shouldReturnNull_whenInvalidTransition() {
        Address address = new Address("Rua A", "Lisboa", "1000-001");
        Admin admin = new Admin("admin", "pass", "Admin", "912345678", "987654321");
        Restaurant restaurant = new Restaurant("123456789", "Cantina", address, admin);
        Client client = new Client("client", "pass", "Client", "912345678", "123456789", new Address("Rua A", "Lisboa", "1000-001"));
        Product product = new Product(9.99, "Mussarela", "Pizza", restaurant);
        Order order = new Order(restaurant, client, List.of(product));

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        assertNull(orderService.updateOrderStatus(1L, "READY"));
    }

    @Test
    void assignDeliveryPerson_shouldReturnNull_whenDeliveryPersonBusy() {
        Address address = new Address("Rua A", "Lisboa", "1000-001");
        Admin admin = new Admin("admin", "pass", "Admin", "912345678", "987654321");
        Restaurant restaurant = new Restaurant("123456789", "Cantina", address, admin);
        Client client = new Client("client", "pass", "Client", "912345678", "123456789", new Address("Rua A", "Lisboa", "1000-001"));
        Product product = new Product(9.99, "Mussarela", "Pizza", restaurant);
        Order order = new Order(restaurant, client, List.of(product));
        order.setOrderStatus(OrderStatus.READY);

        DeliveryPerson delivery = new DeliveryPerson("delivery", "pass", "Delivery", "931234567", "111222333");
        delivery.setDeliveryPersonStatus(DeliveryPersonStatus.BUSY);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(userRepository.findById(2L)).thenReturn(Optional.of(delivery));

        assertNull(orderService.assignDeliveryPerson(1L, 2L));
    }

    @Test
    void cancelOrder_shouldReturnTrue_whenNotExists() {
        when(orderRepository.existsById(1L)).thenReturn(false);

        assertTrue(orderService.cancelOrder(1L));
    }
}
