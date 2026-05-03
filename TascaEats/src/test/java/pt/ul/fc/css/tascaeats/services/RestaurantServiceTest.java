package pt.ul.fc.css.tascaeats.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pt.ul.fc.css.tascaeats.dto.restaurant.RestaurantRequestDto;
import pt.ul.fc.css.tascaeats.dto.restaurant.RestaurantResponseDto;
import pt.ul.fc.css.tascaeats.dto.restaurant.ReviewRequestDto;
import pt.ul.fc.css.tascaeats.dto.restaurant.ReviewResponseDto;
import pt.ul.fc.css.tascaeats.entities.Address;
import pt.ul.fc.css.tascaeats.entities.Admin;
import pt.ul.fc.css.tascaeats.entities.Client;
import pt.ul.fc.css.tascaeats.entities.Menu;
import pt.ul.fc.css.tascaeats.entities.Order;
import pt.ul.fc.css.tascaeats.entities.Product;
import pt.ul.fc.css.tascaeats.entities.ProductItem;
import pt.ul.fc.css.tascaeats.entities.Restaurant;
import pt.ul.fc.css.tascaeats.entities.Review;
import pt.ul.fc.css.tascaeats.entities.enums.ProductStatus;
import pt.ul.fc.css.tascaeats.exceptions.RequirementsNotMetException;
import pt.ul.fc.css.tascaeats.exceptions.ResourceNotFoundException;
import pt.ul.fc.css.tascaeats.repository.MenuRepository;
import pt.ul.fc.css.tascaeats.repository.OrderRepository;
import pt.ul.fc.css.tascaeats.repository.ProductItemRepository;
import pt.ul.fc.css.tascaeats.repository.RestaurantRepository;
import pt.ul.fc.css.tascaeats.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class RestaurantServiceTest {

  @Mock private RestaurantRepository restaurantRepository;
  @Mock private ProductItemRepository productItemRepository;
  @Mock private UserRepository userRepository;
  @Mock private OrderRepository orderRepository;
  @Mock private MenuRepository menuRepository;

  @InjectMocks private RestaurantService restaurantService;

  private Address address() { return new Address("Rua A", "Lisboa", "1000-001"); }
  private Admin admin() { return new Admin("admin", "pass", "Admin", "912345678", "987654321"); }
  private Client client() {
    return new Client("client", "pass", "Client", "912345678", "123456789", List.of(address()));
  }
  private Restaurant restaurant() {
    return new Restaurant("123456789", "Cantina", address(), admin(), LocalTime.of(11, 0), LocalTime.of(22, 0));
  }
  private RestaurantRequestDto restaurantRequest() {
    RestaurantRequestDto r = new RestaurantRequestDto();
    r.setDesignation("Cantina"); r.setNif("123456789");
    r.setCity("Lisboa"); r.setStreet("Rua A"); r.setPostalCode("1000-001");
    r.setAdminId(1L); r.setOpenHours("11:00"); r.setCloseHours("22:00");
    return r;
  }

  // ─── createRestaurant ───────────────────────────────────────────────────────

  @Test
  void createRestaurant_shouldThrow_whenAdminNotFound() {
    when(userRepository.findById(1L)).thenReturn(Optional.empty());
    assertThrows(ResourceNotFoundException.class,
        () -> restaurantService.createRestaurant(restaurantRequest()));
  }

  @Test
  void createRestaurant_shouldThrow_whenUserIsNotAdmin() {
    Client c = client();
    when(userRepository.findById(1L)).thenReturn(Optional.of(c));
    assertThrows(ResourceNotFoundException.class,
        () -> restaurantService.createRestaurant(restaurantRequest()));
  }

  @Test
  void createRestaurant_shouldReturnResponse_whenAdminExists() {
    Admin a = admin();
    Restaurant r = restaurant();
    when(userRepository.findById(1L)).thenReturn(Optional.of(a));
    when(restaurantRepository.save(any(Restaurant.class))).thenReturn(r);
    RestaurantResponseDto result = restaurantService.createRestaurant(restaurantRequest());
    assertNotNull(result);
    assertEquals("Cantina", result.designation());
    assertEquals("Lisboa", result.city());
  }

  // ─── changeStatus ───────────────────────────────────────────────────────────

  @Test
  void changeStatus_shouldThrow_whenNotFound() {
    when(restaurantRepository.findById(99L)).thenReturn(Optional.empty());
    assertThrows(ResourceNotFoundException.class,
        () -> restaurantService.changeStatus(99L, "CLOSED"));
  }

  @Test
  void changeStatus_shouldReturnUpdated_whenFound() {
    Restaurant r = restaurant();
    when(restaurantRepository.findById(1L)).thenReturn(Optional.of(r));
    when(restaurantRepository.save(any(Restaurant.class))).thenAnswer(inv -> inv.getArgument(0));
    RestaurantResponseDto result = restaurantService.changeStatus(1L, "CLOSED");
    assertNotNull(result);
    assertEquals("CLOSED", result.status());
  }

  // ─── assignMenu ─────────────────────────────────────────────────────────────

  @Test
  void assignMenu_shouldThrow_whenRestaurantNotFound() {
    when(restaurantRepository.findById(99L)).thenReturn(Optional.empty());
    assertThrows(ResourceNotFoundException.class,
        () -> restaurantService.assignMenu(99L, 1L));
  }

  @Test
  void assignMenu_shouldThrow_whenMenuNotFound() {
    when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant()));
    when(menuRepository.findById(99L)).thenReturn(Optional.empty());
    assertThrows(ResourceNotFoundException.class,
        () -> restaurantService.assignMenu(1L, 99L));
  }

  @Test
  void assignMenu_shouldReturnUpdated_whenBothFound() {
    Restaurant r = restaurant();
    Menu m = new Menu("MenuA", "Italian", "desc");
    when(restaurantRepository.findById(1L)).thenReturn(Optional.of(r));
    when(menuRepository.findById(1L)).thenReturn(Optional.of(m));
    when(menuRepository.save(any(Menu.class))).thenReturn(m);
    when(restaurantRepository.save(any(Restaurant.class))).thenAnswer(inv -> inv.getArgument(0));
    RestaurantResponseDto result = restaurantService.assignMenu(1L, 1L);
    assertNotNull(result);
  }

  // ─── updateProductStatus ────────────────────────────────────────────────────

  @Test
  void updateProductStatus_shouldThrow_whenProductNotFound() {
    when(productItemRepository.findById(99L)).thenReturn(Optional.empty());
    assertThrows(ResourceNotFoundException.class,
        () -> restaurantService.updateProductStatus(1L, 99L, ProductStatus.SOLD_OUT));
  }

  @Test
  void updateProductStatus_shouldReturnUpdated_whenFound() {
    Restaurant r = restaurant();
    Menu m = new Menu("m", "i", "d");
    Product p = new Product(9.99, "desc", "Pizza", m);
    ProductItem pi = new ProductItem(p, r);
    when(productItemRepository.findById(1L)).thenReturn(Optional.of(pi));
    when(productItemRepository.save(any(ProductItem.class))).thenAnswer(inv -> inv.getArgument(0));
    var result = restaurantService.updateProductStatus(1L, 1L, ProductStatus.SOLD_OUT);
    assertNotNull(result);
    assertEquals("SOLD_OUT", result.availability());
  }

  // ─── deleteProductItem ──────────────────────────────────────────────────────

  @Test
  void deleteProductItem_shouldReturnFalse_whenNotFromRestaurant() {
    when(productItemRepository.existsByIdAndRestaurantId(99L, 1L)).thenReturn(false);
    assertFalse(restaurantService.deleteProductItem(1L, 99L));
  }

  @Test
  void deleteProductItem_shouldDeletePermanently_whenNoOrders() {
    when(productItemRepository.existsByIdAndRestaurantId(1L, 1L)).thenReturn(true);
    when(orderRepository.existsByProductItemsId(1L)).thenReturn(false);
    assertTrue(restaurantService.deleteProductItem(1L, 1L));
    verify(productItemRepository).deleteById(1L);
  }

  @Test
  void deleteProductItem_shouldReturnFalse_whenHasActiveOrders() {
    when(productItemRepository.existsByIdAndRestaurantId(1L, 1L)).thenReturn(true);
    when(orderRepository.existsByProductItemsId(1L)).thenReturn(true);
    when(orderRepository.existsByProductItemsIdAndStatusNotIn(any(), any())).thenReturn(true);
    assertFalse(restaurantService.deleteProductItem(1L, 1L));
    verify(productItemRepository, never()).deleteById(any());
  }

  @Test
  void deleteProductItem_shouldSoftDelete_whenHasOnlyCompletedOrders() {
    Restaurant r = restaurant();
    Menu m = new Menu("m", "i", "d");
    Product p = new Product(9.99, "desc", "Pizza", m);
    ProductItem pi = new ProductItem(p, r);
    when(productItemRepository.existsByIdAndRestaurantId(1L, 1L)).thenReturn(true);
    when(orderRepository.existsByProductItemsId(1L)).thenReturn(true);
    when(orderRepository.existsByProductItemsIdAndStatusNotIn(any(), any())).thenReturn(false);
    when(productItemRepository.findById(1L)).thenReturn(Optional.of(pi));
    when(productItemRepository.save(any(ProductItem.class))).thenReturn(pi);
    assertTrue(restaurantService.deleteProductItem(1L, 1L));
    verify(productItemRepository, never()).deleteById(any());
    assertNotNull(pi.getDeletedAt());
  }

  // ─── addReview ──────────────────────────────────────────────────────────────

  @Test
  void addReview_shouldThrow_whenRestaurantNotFound() {
    when(restaurantRepository.findById(99L)).thenReturn(Optional.empty());
    ReviewRequestDto req = new ReviewRequestDto();
    assertThrows(ResourceNotFoundException.class, () -> restaurantService.addReview(99L, req));
  }

  @Test
  void addReview_shouldThrow_whenClientNotFound() {
    Restaurant r = restaurant();
    when(restaurantRepository.findById(1L)).thenReturn(Optional.of(r));
    ReviewRequestDto req = new ReviewRequestDto();
    req.setClientId(99L); req.setRating(4); req.setComment("Good");
    when(userRepository.findById(99L)).thenReturn(Optional.empty());
    assertThrows(ResourceNotFoundException.class, () -> restaurantService.addReview(1L, req));
  }

  @Test
  void addReview_shouldThrow_whenClientNeverOrdered() {
    Restaurant r = restaurant();
    Client c = client();
    when(restaurantRepository.findById(1L)).thenReturn(Optional.of(r));
    when(userRepository.findById(1L)).thenReturn(Optional.of(c));
    ReviewRequestDto req = new ReviewRequestDto();
    req.setClientId(1L); req.setRating(4); req.setComment("Good");
    // restaurant has no orders from this client
    assertThrows(RequirementsNotMetException.class, () -> restaurantService.addReview(1L, req));
  }

  @Test
  void addReview_shouldThrow_whenRatingOutOfRange() {
    Client c = client();
    // Build restaurant with an order from this client
    Restaurant r = restaurant();
    Order order = new Order(c, new ArrayList<>(), address());
    r.addOrder(order);

    when(restaurantRepository.findById(1L)).thenReturn(Optional.of(r));
    when(userRepository.findById(1L)).thenReturn(Optional.of(c));

    ReviewRequestDto req = new ReviewRequestDto();
    req.setClientId(1L); req.setRating(6); req.setComment("Too high");
    assertThrows(RequirementsNotMetException.class, () -> restaurantService.addReview(1L, req));
  }

  @Test
  void addReview_shouldReturnReview_whenValid() {
    Client c = client();
    Restaurant r = restaurant();
    Order order = new Order(c, new ArrayList<>(), address());
    r.addOrder(order);

    when(restaurantRepository.findById(1L)).thenReturn(Optional.of(r));
    when(userRepository.findById(1L)).thenReturn(Optional.of(c));
    when(restaurantRepository.save(any(Restaurant.class))).thenReturn(r);

    ReviewRequestDto req = new ReviewRequestDto();
    req.setClientId(1L); req.setRating(4); req.setComment("Great food");

    ReviewResponseDto result = restaurantService.addReview(1L, req);
    assertNotNull(result);
    assertEquals(4, result.rating());
    assertEquals("Great food", result.comment());
  }

  // ─── getReviews ─────────────────────────────────────────────────────────────

  @Test
  void getReviews_shouldThrow_whenRestaurantNotFound() {
    when(restaurantRepository.findById(99L)).thenReturn(Optional.empty());
    assertThrows(ResourceNotFoundException.class, () -> restaurantService.getReviews(99L));
  }

  @Test
  void getReviews_shouldReturnList_whenFound() {
    Client c = client();
    Restaurant r = restaurant();
    Review review = new Review(5, "Excellent", c, r);
    r.addReview(review);
    when(restaurantRepository.findById(1L)).thenReturn(Optional.of(r));
    List<ReviewResponseDto> result = restaurantService.getReviews(1L);
    assertEquals(1, result.size());
    assertEquals(5, result.get(0).rating());
  }

  @Test
  void getReviews_shouldReturnEmptyList_whenNoReviews() {
    Restaurant r = restaurant();
    when(restaurantRepository.findById(1L)).thenReturn(Optional.of(r));
    List<ReviewResponseDto> result = restaurantService.getReviews(1L);
    assertTrue(result.isEmpty());
  }
}