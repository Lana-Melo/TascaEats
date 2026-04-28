package pt.ul.fc.css.tascaeats.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import pt.ul.fc.css.tascaeats.dto.product.ProductRequest;
import pt.ul.fc.css.tascaeats.dto.product.ProductResponse;
import pt.ul.fc.css.tascaeats.dto.restaurant.RestaurantRequest;
import pt.ul.fc.css.tascaeats.dto.restaurant.RestaurantResponse;
import pt.ul.fc.css.tascaeats.entities.Address;
import pt.ul.fc.css.tascaeats.entities.Admin;
import pt.ul.fc.css.tascaeats.entities.Product;
import pt.ul.fc.css.tascaeats.entities.Restaurant;
import pt.ul.fc.css.tascaeats.repository.OrderRepository;
import pt.ul.fc.css.tascaeats.repository.ProductRepository;
import pt.ul.fc.css.tascaeats.repository.RestaurantRepository;
import pt.ul.fc.css.tascaeats.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class RestaurantServiceTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private RestaurantService restaurantService;

    @Test
    void searchByDesignationOrCity_shouldReturnAllWhenNoFilters() {
        Address address = new Address("Rua A", "Lisboa", "1000-001");
        Admin admin = new Admin("admin", "pass", "Admin", "912345678", "987654321");
        Restaurant restaurant = new Restaurant("123456789", "Cantina", address, admin, LocalTime.of(11, 0), LocalTime.of(22, 0));

        when(restaurantRepository.findAll()).thenReturn(List.of(restaurant));

        List<RestaurantResponse> results = restaurantService.searchByDesignationOrCity(null, null);

        assertEquals(1, results.size());
        assertEquals("Cantina", results.get(0).designation());
    }

    @Test
    void searchByDesignationOrCity_shouldReturnEmptyList_whenFindAllReturnsEmpty() {
        when(restaurantRepository.findAll()).thenReturn(Collections.emptyList());

        List<RestaurantResponse> results = restaurantService.searchByDesignationOrCity(null, null);

        assertTrue(results.isEmpty());
    }

    @Test
    void searchByDesignationOrCity_shouldReturnAllWhenBothEmptyStrings() {
        Address address = new Address("Rua A", "Lisboa", "1000-001");
        Admin admin = new Admin("admin", "pass", "Admin", "912345678", "987654321");
        Restaurant restaurant = new Restaurant("123456789", "Cantina", address, admin, LocalTime.of(11, 0), LocalTime.of(22, 0));

        when(restaurantRepository.findAll()).thenReturn(List.of(restaurant));

        List<RestaurantResponse> results = restaurantService.searchByDesignationOrCity("", "");

        assertEquals(1, results.size());
        assertEquals("Cantina", results.get(0).designation());
    }

    @Test
    void searchByDesignationOrCity_shouldReturnByDesignationOnly() {
        Address address = new Address("Rua B", "Porto", "4000-001");
        Admin admin = new Admin("admin", "pass", "Admin", "912345678", "987654321");
        Restaurant restaurant = new Restaurant("987654321", "Pizza House", address, admin, LocalTime.of(11, 0), LocalTime.of(22, 0));

        when(restaurantRepository.findByDesignationContainingIgnoreCase(eq("Pizza")))
            .thenReturn(Optional.of(List.of(restaurant)));

        List<RestaurantResponse> results = restaurantService.searchByDesignationOrCity("Pizza", null);

        assertEquals(1, results.size());
        assertEquals("Pizza House", results.get(0).designation());
    }

    @Test
    void searchByDesignationOrCity_shouldReturnByCityOnly() {
        Address address = new Address("Rua C", "Coimbra", "3000-001");
        Admin admin = new Admin("admin", "pass", "Admin", "912345678", "987654321");
        Restaurant restaurant = new Restaurant("111222333", "Tasca", address, admin, LocalTime.of(11, 0), LocalTime.of(22, 0));

        when(restaurantRepository.findByAddressCityContainingIgnoreCase(eq("Coimbra")))
            .thenReturn(Optional.of(List.of(restaurant)));

        List<RestaurantResponse> results = restaurantService.searchByDesignationOrCity(null, "Coimbra");

        assertEquals(1, results.size());
        assertEquals("Tasca", results.get(0).designation());
        assertEquals("Coimbra", results.get(0).city());
    }

    @Test
    void createRestaurant_shouldReturnNull_whenAdminNotFound() {
        RestaurantRequest request = new RestaurantRequest();
        request.setDesignation("Cantina");
        request.setNif("123456789");
        request.setCity("Lisboa");
        request.setStreet("Rua A");
        request.setPostalCode("1000-001");
        request.setAdminId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertNull(restaurantService.createRestaurant(request));
    }

    @Test
    void createRestaurant_shouldReturnResponse_whenAdminExists() {
        RestaurantRequest request = new RestaurantRequest();
        request.setDesignation("Cantina");
        request.setNif("123456789");
        request.setCity("Lisboa");
        request.setStreet("Rua A");
        request.setPostalCode("1000-001");
        request.setAdminId(1L);

        Admin admin = new Admin("admin", "pass", "Admin", "912345678", "987654321");
        when(userRepository.findById(1L)).thenReturn(Optional.of(admin));
        when(restaurantRepository.save(any(Restaurant.class))).thenAnswer(invocation -> invocation.getArgument(0));

        RestaurantResponse response = restaurantService.createRestaurant(request);

        assertNotNull(response);
        assertEquals("Cantina", response.designation());
        assertEquals("Lisboa", response.city());
    }

    @Test
    void addProduct_shouldReturnNull_whenRestaurantMissing() {
        ProductRequest request = new ProductRequest();
        request.setDesignation("Pizza");
        request.setDescription("Mussarela");
        request.setPrice(9.99);
        request.setAvailability("AVAILABLE");

        when(restaurantRepository.findById(1L)).thenReturn(Optional.empty());

        assertNull(restaurantService.addProduct(1L, request));
    }

    @Test
    void addProduct_shouldReturnProductResponse_whenRestaurantExists() {
        ProductRequest request = new ProductRequest();
        request.setDesignation("Pizza");
        request.setDescription("Mussarela");
        request.setPrice(9.99);
        request.setAvailability("AVAILABLE");

        Address address = new Address("Rua A", "Lisboa", "1000-001");
        Admin admin = new Admin("admin", "pass", "Admin", "912345678", "987654321");
        Restaurant restaurant = new Restaurant("123456789", "Cantina", address, admin, LocalTime.of(11, 0), LocalTime.of(22, 0));

        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ProductResponse response = restaurantService.addProduct(1L, request);

        assertNotNull(response);
        assertEquals("Pizza", response.designation());
        assertEquals("AVAILABLE", response.availability());
    }

    @Test
    void getProducts_shouldReturnEmptyList_whenNoProductsFound() {
        when(productRepository.findAllByRestaurantId(1L)).thenReturn(Optional.empty());

        List<ProductResponse> products = restaurantService.getProducts(1L);

        assertTrue(products.isEmpty());
    }

    @Test
    void getProducts_shouldReturnProductList_whenProductsExist() {
        Address address = new Address("Rua A", "Lisboa", "1000-001");
        Admin admin = new Admin("admin", "pass", "Admin", "912345678", "987654321");
        Restaurant restaurant = new Restaurant("123456789", "Cantina", address, admin, LocalTime.of(11, 0), LocalTime.of(22, 0));
        Product product = new Product(9.99, "Mussarela", "Pizza", restaurant);

        when(productRepository.findAllByRestaurantId(1L)).thenReturn(Optional.of(List.of(product)));

        List<ProductResponse> products = restaurantService.getProducts(1L);

        assertEquals(1, products.size());
        assertEquals("Pizza", products.get(0).designation());
    }

    @Test
    void updateProduct_shouldReturnNull_whenProductNotFound() {
        ProductRequest request = new ProductRequest();
        request.setDesignation("Pizza Updated");
        request.setDescription("Mussarela Updated");
        request.setPrice(12.99);
        request.setAvailability("AVAILABLE");

        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertNull(restaurantService.updateProduct(1L, 1L, request));
    }

    @Test
    void updateProduct_shouldReturnProductResponse_whenProductExists() {
        ProductRequest request = new ProductRequest();
        request.setDesignation("Pizza Updated");
        request.setDescription("Mussarela Updated");
        request.setPrice(12.99);
        request.setAvailability("SOLD_OUT");

        Address address = new Address("Rua A", "Lisboa", "1000-001");
        Admin admin = new Admin("admin", "pass", "Admin", "912345678", "987654321");
        Restaurant restaurant = new Restaurant("123456789", "Cantina", address, admin, LocalTime.of(11, 0), LocalTime.of(22, 0));
        Product product = new Product(9.99, "Mussarela", "Pizza", restaurant);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ProductResponse response = restaurantService.updateProduct(1L, 1L, request);

        assertNotNull(response);
        assertEquals("Pizza Updated", response.designation());
        assertEquals("SOLD_OUT", response.availability());
    }

    @Test
    void deleteProduct_shouldReturnFalse_whenProductNotFromRestaurant() {
        when(productRepository.existsByIdAndRestaurantId(1L, 1L)).thenReturn(false);

        boolean result = restaurantService.deleteProduct(1L, 1L);

        assertEquals(false, result);
    }

    @Test
    void deleteProduct_shouldSoftDelete_whenProductHasOnlyCompletedOrders() {
        Address address = new Address("Rua A", "Lisboa", "1000-001");
        Admin admin = new Admin("admin", "pass", "Admin", "912345678", "987654321");
        Restaurant restaurant = new Restaurant("123456789", "Cantina", address, admin, LocalTime.of(11, 0), LocalTime.of(22, 0));
        Product product = new Product(9.99, "Mussarela", "Pizza", restaurant);

        when(productRepository.existsByIdAndRestaurantId(1L, 1L)).thenReturn(true);
        when(orderRepository.existsByProductsId(1L)).thenReturn(true);
        when(orderRepository.existsByProductsIdAndStatusNotIn(eq(1L), any())).thenReturn(false);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenAnswer(inv -> inv.getArgument(0));

        assertTrue(restaurantService.deleteProduct(1L, 1L));
        assertNotNull(product.getDeletedAt());
    }

    @Test
    void deleteProduct_shouldReturnFalse_whenProductHasOrdersInProgress() {
        when(productRepository.existsByIdAndRestaurantId(1L, 1L)).thenReturn(true);
        when(orderRepository.existsByProductsId(1L)).thenReturn(true);
        when(orderRepository.existsByProductsIdAndStatusNotIn(eq(1L), any())).thenReturn(true);

        assertFalse(restaurantService.deleteProduct(1L, 1L));
    }

    @Test
    void deleteProduct_shouldHardDelete_whenProductHasNoOrders() {
        when(productRepository.existsByIdAndRestaurantId(1L, 1L)).thenReturn(true);
        when(orderRepository.existsByProductsId(1L)).thenReturn(false);

        boolean result = restaurantService.deleteProduct(1L, 1L);

        assertEquals(true, result);
    }

    @Test
    void changeStatus_shouldReturnNull_whenRestaurantNotFound() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.empty());

        assertNull(restaurantService.changeStatus(1L, "OPEN"));
    }

    @Test
    void changeStatus_shouldReturnResponse_whenRestaurantExists() {
        Address address = new Address("Rua A", "Lisboa", "1000-001");
        Admin admin = new Admin("admin", "pass", "Admin", "912345678", "987654321");
        Restaurant restaurant = new Restaurant("123456789", "Cantina", address, admin, LocalTime.of(11, 0), LocalTime.of(22, 0));

        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
        when(restaurantRepository.save(any(Restaurant.class))).thenAnswer(invocation -> invocation.getArgument(0));

        RestaurantResponse response = restaurantService.changeStatus(1L, "closed");

        assertNotNull(response);
        assertEquals("CLOSED", response.status());
    }

    @Test
    void searchByDesignationOrCity_shouldReturnByBothDesignationAndCity() {
        Address address = new Address("Rua B", "Porto", "4000-001");
        Admin admin = new Admin("admin", "pass", "Admin", "912345678", "987654321");
        Restaurant restaurant = new Restaurant("987654321", "Pizza House", address, admin, LocalTime.of(11, 0), LocalTime.of(22, 0));

        when(restaurantRepository.findByDesignationContainingIgnoreCaseAndAddressCityContainingIgnoreCase(eq("Pizza"), eq("Porto")))
            .thenReturn(Optional.of(List.of(restaurant)));

        List<RestaurantResponse> results = restaurantService.searchByDesignationOrCity("Pizza", "Porto");

        assertEquals(1, results.size());
        assertEquals("Pizza House", results.get(0).designation());
        assertEquals("Porto", results.get(0).city());
    }

    @Test
    void searchByDesignationOrCity_shouldReturnEmptyList_whenNoMatchesFound() {
        when(restaurantRepository.findByDesignationContainingIgnoreCase(eq("NonExistent")))
            .thenReturn(Optional.empty());

        List<RestaurantResponse> results = restaurantService.searchByDesignationOrCity("NonExistent", null);

        assertTrue(results.isEmpty());
    }

    @Test
    void searchByDesignationOrCity_shouldReturnEmptyList_whenRepositoryReturnsEmptyList() {
        when(restaurantRepository.findByDesignationContainingIgnoreCase(eq("Pizza")))
            .thenReturn(Optional.of(Collections.emptyList()));

        List<RestaurantResponse> results = restaurantService.searchByDesignationOrCity("Pizza", null);

        assertTrue(results.isEmpty());
    }

    @Test
    void searchByDesignationOrCity_shouldReturnByCity_whenDesignationIsEmpty() {
        Address address = new Address("Rua C", "Coimbra", "3000-001");
        Admin admin = new Admin("admin", "pass", "Admin", "912345678", "987654321");
        Restaurant restaurant = new Restaurant("111222333", "Tasca", address, admin, LocalTime.of(11, 0), LocalTime.of(22, 0));

        when(restaurantRepository.findByDesignationContainingIgnoreCaseAndAddressCityContainingIgnoreCase(eq(""), eq("Coimbra")))
            .thenReturn(Optional.of(List.of(restaurant)));

        List<RestaurantResponse> results = restaurantService.searchByDesignationOrCity("", "Coimbra");

        assertEquals(1, results.size());
        assertEquals("Tasca", results.get(0).designation());
        assertEquals("Coimbra", results.get(0).city());
    }

    @Test
    void searchByDesignationOrCity_shouldReturnByCity_whenDesignationIsNull() {
        Address address = new Address("Rua C", "Coimbra", "3000-001");
        Admin admin = new Admin("admin", "pass", "Admin", "912345678", "987654321");
        Restaurant restaurant = new Restaurant("111222333", "Tasca", address, admin, LocalTime.of(11, 0), LocalTime.of(22, 0));

        when(restaurantRepository.findByAddressCityContainingIgnoreCase(eq("Coimbra")))
            .thenReturn(Optional.of(List.of(restaurant)));

        List<RestaurantResponse> results = restaurantService.searchByDesignationOrCity(null, "Coimbra");

        assertEquals(1, results.size());
        assertEquals("Tasca", results.get(0).designation());
        assertEquals("Coimbra", results.get(0).city());
    }

    @Test
    void searchByDesignationOrCity_shouldReturnByDesignation_whenCityIsNull() {
        Address address = new Address("Rua B", "Porto", "4000-001");
        Admin admin = new Admin("admin", "pass", "Admin", "912345678", "987654321");
        Restaurant restaurant = new Restaurant("987654321", "Pizza House", address, admin, LocalTime.of(11, 0), LocalTime.of(22, 0));

        when(restaurantRepository.findByDesignationContainingIgnoreCase(eq("Pizza")))
            .thenReturn(Optional.of(List.of(restaurant)));

        List<RestaurantResponse> results = restaurantService.searchByDesignationOrCity("Pizza", null);

        assertEquals(1, results.size());
        assertEquals("Pizza House", results.get(0).designation());
    }

    @Test
    void searchByDesignationOrCity_shouldReturnByDesignation_whenCityIsEmpty() {
        Address address = new Address("Rua B", "Porto", "4000-001");
        Admin admin = new Admin("admin", "pass", "Admin", "912345678", "987654321");
        Restaurant restaurant = new Restaurant("987654321", "Pizza House", address, admin, LocalTime.of(11, 0), LocalTime.of(22, 0));

        when(restaurantRepository.findByDesignationContainingIgnoreCaseAndAddressCityContainingIgnoreCase(eq("Pizza"), eq("")))
            .thenReturn(Optional.of(List.of(restaurant)));

        List<RestaurantResponse> results = restaurantService.searchByDesignationOrCity("Pizza", "");

        assertEquals(1, results.size());
        assertEquals("Pizza House", results.get(0).designation());
    }

    @Test
    void createRestaurant_shouldReturnNull_whenUserNotAdmin() {
        RestaurantRequest request = new RestaurantRequest();
        request.setDesignation("Cantina");
        request.setNif("123456789");
        request.setCity("Lisboa");
        request.setStreet("Rua A");
        request.setPostalCode("1000-001");
        request.setAdminId(1L);

        // Mock a user that is not an Admin (e.g., a Client)
        Address clientAddress = new Address("Rua Cliente", "Lisboa", "1000-002");
        pt.ul.fc.css.tascaeats.entities.Client client = new pt.ul.fc.css.tascaeats.entities.Client("client", "pass", "Client", "912345678", "111222333", List.of(clientAddress));
        when(userRepository.findById(1L)).thenReturn(Optional.of(client));

        assertNull(restaurantService.createRestaurant(request));
    }
}
