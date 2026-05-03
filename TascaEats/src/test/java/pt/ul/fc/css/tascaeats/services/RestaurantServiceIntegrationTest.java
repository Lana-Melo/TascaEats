package pt.ul.fc.css.tascaeats.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import pt.ul.fc.css.tascaeats.dto.product.ProductItemResponseDto;
import pt.ul.fc.css.tascaeats.dto.restaurant.RestaurantResponseDto;
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
import pt.ul.fc.css.tascaeats.repository.MenuRepository;
import pt.ul.fc.css.tascaeats.repository.OrderRepository;
import pt.ul.fc.css.tascaeats.repository.ProductItemRepository;
import pt.ul.fc.css.tascaeats.repository.ProductRepository;
import pt.ul.fc.css.tascaeats.repository.RestaurantRepository;
import pt.ul.fc.css.tascaeats.repository.UserRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class RestaurantServiceIntegrationTest {

  @Autowired private RestaurantRepository restaurantRepository;
  @Autowired private UserRepository userRepository;
  @Autowired private MenuRepository menuRepository;
  @Autowired private ProductItemRepository productItemRepository;
  @Autowired private ProductRepository productRepository;
  @Autowired private OrderRepository orderRepository;

  private RestaurantService restaurantService;

  private Admin admin;
  private Restaurant restaurant1;
  private Restaurant restaurant2;
  private Menu italianMenu;
  private Menu frenchMenu;

  @BeforeEach
  void setup() {
    restaurantService =
        new RestaurantService(
            restaurantRepository,
            userRepository,
            menuRepository,
            productItemRepository,
            orderRepository);

    // Save admin
    admin = new Admin("admin", "pass", "Admin User", "912345678", "987654321");
    admin = userRepository.save(admin);

    // Save menus
    italianMenu = new Menu("Italian Menu", "Italian", "Italian dishes");
    frenchMenu = new Menu("French Menu", "French", "French cuisine");
    italianMenu = menuRepository.save(italianMenu);
    frenchMenu = menuRepository.save(frenchMenu);

    // Save products and associate to menus
    Product arrozDoce =
        productRepository.save(
            new Product(2.50, "sobremesa simples que brunao recomenda", "arroz doce", frenchMenu));
    frenchMenu.addProduct(arrozDoce);
    menuRepository.save(frenchMenu);

    Product arrozDoceDois =
        productRepository.save(
            new Product(2.50, "sobremesa simples que brunao recomenda", "arroz doce", italianMenu));
    italianMenu.addProduct(arrozDoceDois);
    menuRepository.save(italianMenu);

    // Save client before creating orders/reviews
    Address lisboaAddress = new Address("Rua A", "Lisboa", "1000-001");
    Address portoAddress = new Address("Rua B", "Porto", "4000-001");
    Client client =
        new Client(
            "client", "pass", "Client User", "912345677", "123456788", List.of(lisboaAddress));
    client = userRepository.save(client);

    // Create restaurant1: add ProductItem to collection BEFORE saving (cascade = ALL +
    // orphanRemoval)
    restaurant1 =
        new Restaurant(
            "111111111", "Cantina", lisboaAddress, admin, LocalTime.of(11, 0), LocalTime.of(22, 0));
    restaurant1.setMenu(italianMenu);
    ProductItem piArrozDoceDois = new ProductItem(arrozDoce, restaurant1);
    restaurant1.addProductItem(piArrozDoceDois);
    restaurant1 = restaurantRepository.save(restaurant1);
    piArrozDoceDois =
        restaurant1.getProductItems().get(0); // get the managed reference after cascade save

    // Create restaurant2: same pattern
    restaurant2 =
        new Restaurant(
            "222222222",
            "Ristorante",
            portoAddress,
            admin,
            LocalTime.of(12, 0),
            LocalTime.of(23, 0));
    restaurant2.setMenu(frenchMenu);
    ProductItem piArrozDoce = new ProductItem(arrozDoce, restaurant2);
    restaurant2.addProductItem(piArrozDoce);
    restaurant2 = restaurantRepository.save(restaurant2);
    piArrozDoce = restaurant2.getProductItems().get(0);

    // Add reviews
    restaurant1.addReview(new Review(5, "Excellent", client, restaurant1));
    restaurant1.addReview(new Review(4, "Good", client, restaurant1));
    restaurant1 = restaurantRepository.save(restaurant1);

    restaurant2.addReview(new Review(3, "Ok", client, restaurant2));
    restaurant2 = restaurantRepository.save(restaurant2);

    // Save orders via orderRepository BEFORE adding to restaurant (ManyToMany, no cascade)
    Order order1 = orderRepository.save(new Order(client, List.of(piArrozDoceDois), lisboaAddress));
    Order order2 = orderRepository.save(new Order(client, List.of(piArrozDoceDois), lisboaAddress));
    restaurant1.addOrder(order1);
    restaurant1.addOrder(order2);
    restaurant1 = restaurantRepository.save(restaurant1);

    Order order3 = orderRepository.save(new Order(client, List.of(piArrozDoce), portoAddress));
    restaurant2.addOrder(order3);
    restaurant2 = restaurantRepository.save(restaurant2);
  }

  // ─── searchBy Filter Tests ──────────────────────────────────────────────────

  @Test
  void searchBy_shouldFilter_byDesignation() {
    List<RestaurantResponseDto> result =
        restaurantService.searchBy("Cantina", null, null, null, null, null, null, null, null);

    assertEquals(1, result.size());
    assertEquals("Cantina", result.get(0).designation());
  }

  @Test
  void searchBy_shouldFilter_byDesignationPartial() {
    List<RestaurantResponseDto> result =
        restaurantService.searchBy("tina", null, null, null, null, null, null, null, null);

    assertEquals(1, result.size());
    assertEquals("Cantina", result.get(0).designation());
  }

  @Test
  void searchBy_shouldFilter_byCity() {
    List<RestaurantResponseDto> result =
        restaurantService.searchBy(null, "Lisboa", null, null, null, null, null, null, null);

    assertEquals(1, result.size());
    assertEquals("Lisboa", result.get(0).city());
  }

  @Test
  void searchBy_shouldFilter_byCityPartial() {
    List<RestaurantResponseDto> result =
        restaurantService.searchBy(null, "boa", null, null, null, null, null, null, null);

    assertEquals(1, result.size());
    assertEquals("Lisboa", result.get(0).city());
  }

  @Test
  void searchBy_shouldFilter_byStreet() {
    List<RestaurantResponseDto> result =
        restaurantService.searchBy(null, null, "Rua A", null, null, null, null, null, null);

    assertEquals(1, result.size());
    assertEquals("Rua A", result.get(0).street());
  }

  @Test
  void searchBy_shouldFilter_byPostalCode() {
    List<RestaurantResponseDto> result =
        restaurantService.searchBy(null, null, null, "1000-001", null, null, null, null, null);

    assertEquals(1, result.size());
    assertEquals("1000-001", result.get(0).postalCode());
  }

  @Test
  void searchBy_shouldFilter_byCuisine() {
    List<RestaurantResponseDto> result =
        restaurantService.searchBy(null, null, null, null, "Italian", null, null, null, null);

    assertEquals(1, result.size());
    assertEquals("Cantina", result.get(0).designation());
  }

  @Test
  void searchBy_shouldFilter_byCuisinePartial() {
    List<RestaurantResponseDto> result =
        restaurantService.searchBy(null, null, null, null, "ian", null, null, null, null);

    assertEquals(1, result.size());
    assertEquals("Cantina", result.get(0).designation());
  }

  @Test
  void searchBy_shouldFilter_byOrderNumber() {
    List<RestaurantResponseDto> result =
        restaurantService.searchBy(null, null, null, null, null, "2", null, null, null);

    assertEquals(1, result.size());
    assertEquals("Cantina", result.get(0).designation());
  }

  @Test
  void searchBy_shouldNotReturn_whenOrderNumberTooHigh() {
    List<RestaurantResponseDto> result =
        restaurantService.searchBy(null, null, null, null, null, "3", null, null, null);

    assertTrue(result.isEmpty());
  }

  @Test
  void searchBy_shouldFilter_byReviewNumber() {
    List<RestaurantResponseDto> result =
        restaurantService.searchBy(null, null, null, null, null, null, "2", null, null);

    assertEquals(1, result.size());
    assertEquals("Cantina", result.get(0).designation());
  }

  @Test
  void searchBy_shouldNotReturn_whenReviewNumberTooHigh() {
    List<RestaurantResponseDto> result =
        restaurantService.searchBy(null, null, null, null, null, null, "3", null, null);

    assertTrue(result.isEmpty());
  }

  @Test
  void searchBy_shouldFilter_byActiveHour() {
    List<RestaurantResponseDto> result =
        restaurantService.searchBy(null, null, null, null, null, null, null, "11:30", null);

    assertEquals(1, result.size());
    assertEquals("Cantina", result.get(0).designation());
  }

  @Test
  void searchBy_shouldNotReturn_whenHourOutsideRange() {
    // Time is outside restaurant1 hours (11:00-22:00)
    List<RestaurantResponseDto> result =
        restaurantService.searchBy(null, null, null, null, null, null, null, "23:00", null);

    assertEquals(1, result.size()); // restaurant2 is open at 23:00 (12:00-23:00)
    assertEquals("Ristorante", result.get(0).designation());
  }

  @Test
  void searchBy_shouldNotReturn_whenAverageTooHigh() {
    List<RestaurantResponseDto> result =
        restaurantService.searchBy(null, null, null, null, null, null, null, null, "1.00");

    assertTrue(result.isEmpty());
  }

  @Test
  void searchBy_shouldFilter_withMultipleFilters() {
    List<RestaurantResponseDto> result =
        restaurantService.searchBy(
            "Cantina", "Lisboa", "Rua A", "1000-001", "Italian", "1", "1", "15:00", null);

    assertEquals(1, result.size());
    assertEquals("Cantina", result.get(0).designation());
  }

  @Test
  void searchBy_shouldReturnEmpty_whenNoMatches() {
    List<RestaurantResponseDto> result =
        restaurantService.searchBy("NonExistent", null, null, null, null, null, null, null, null);

    assertTrue(result.isEmpty());
  }

  @Test
  void searchBy_shouldReturnAll_whenAllFiltersNull() {
    List<RestaurantResponseDto> result =
        restaurantService.searchBy(null, null, null, null, null, null, null, null, null);

    assertEquals(2, result.size());
  }

  // ─── searchProductBy Filter Tests ───────────────────────────────────────────

  @Test
  void searchProductBy_shouldFilter_byDesignation() {
    Product pizza =
        productRepository.save(new Product(12.50, "Delicious pizza", "Pizza", italianMenu));
    Product pasta = productRepository.save(new Product(10.50, "Fresh pasta", "Pasta", italianMenu));
    italianMenu.addProduct(pizza);
    italianMenu.addProduct(pasta);
    menuRepository.save(italianMenu);

    ProductItem pizzaItem = new ProductItem(pizza, restaurant1);
    ProductItem pastaItem = new ProductItem(pasta, restaurant1);
    productItemRepository.save(pizzaItem);
    productItemRepository.save(pastaItem);

    List<ProductItemResponseDto> result =
        restaurantService.searchProductBy(restaurant1.getId(), null, null, "Pizza", null, null);

    assertEquals(1, result.size());
    assertEquals("Pizza", result.get(0).designation());
  }

  @Test
  void searchProductBy_shouldFilter_byDesignationPartial() {
    Product pizza =
        productRepository.save(new Product(12.50, "Delicious pizza", "Pizza", italianMenu));
    italianMenu.addProduct(pizza);
    menuRepository.save(italianMenu);

    ProductItem pizzaItem = new ProductItem(pizza, restaurant1);
    productItemRepository.save(pizzaItem);

    List<ProductItemResponseDto> result =
        restaurantService.searchProductBy(restaurant1.getId(), null, null, "izza", null, null);

    assertEquals(1, result.size());
    assertEquals("Pizza", result.get(0).designation());
  }

  @Test
  void searchProductBy_shouldFilter_byDescription() {
    Product pizza =
        productRepository.save(new Product(12.50, "Tasty Italian pizza", "Pizza", italianMenu));
    italianMenu.addProduct(pizza);
    menuRepository.save(italianMenu);

    ProductItem pizzaItem = new ProductItem(pizza, restaurant1);
    productItemRepository.save(pizzaItem);

    List<ProductItemResponseDto> result =
        restaurantService.searchProductBy(restaurant1.getId(), null, null, "Tasty", null, null);

    assertEquals(1, result.size());
    assertEquals("Pizza", result.get(0).designation());
  }

  @Test
  void searchProductBy_shouldFilter_byPrice() {
    Product expensive =
        productRepository.save(new Product(50.00, "Expensive dish", "Steak", italianMenu));
    Product cheap = productRepository.save(new Product(5.00, "Cheap dish", "Soup", italianMenu));
    italianMenu.addProduct(expensive);
    italianMenu.addProduct(cheap);
    menuRepository.save(italianMenu);

    ProductItem expensiveItem = new ProductItem(expensive, restaurant1);
    ProductItem cheapItem = new ProductItem(cheap, restaurant1);
    productItemRepository.save(expensiveItem);
    productItemRepository.save(cheapItem);

    List<ProductItemResponseDto> result =
        restaurantService.searchProductBy(restaurant1.getId(), "10.00", null, null, null, null);

    assertEquals(2, result.size());
    assertEquals("Soup", result.get(1).designation());
  }

  @Test
  void searchProductBy_shouldFilter_byAvailability() {
    Product pizza =
        productRepository.save(new Product(12.50, "Delicious pizza", "Pizza", italianMenu));
    italianMenu.addProduct(pizza);
    menuRepository.save(italianMenu);

    ProductItem pizzaItem = new ProductItem(pizza, restaurant1);
    pizzaItem.setStatus(ProductStatus.AVAILABLE);
    productItemRepository.save(pizzaItem);

    Product soldOut =
        productRepository.save(new Product(10.00, "Sold out item", "Burger", italianMenu));
    italianMenu.addProduct(soldOut);
    menuRepository.save(italianMenu);

    ProductItem soldOutItem = new ProductItem(soldOut, restaurant1);
    soldOutItem.setStatus(ProductStatus.SOLD_OUT);
    productItemRepository.save(soldOutItem);

    List<ProductItemResponseDto> result =
        restaurantService.searchProductBy(restaurant1.getId(), null, null, null, "AVAILABLE", null);

    assertEquals(2, result.size());
    assertEquals("AVAILABLE", result.get(0).availability());
  }

  @Test
  void searchProductBy_shouldFilter_byRestaurant() {
    Product pizza =
        productRepository.save(new Product(12.50, "Delicious pizza", "Pizza", italianMenu));
    italianMenu.addProduct(pizza);
    menuRepository.save(italianMenu);

    ProductItem item1 = new ProductItem(pizza, restaurant1);
    ProductItem item2 = new ProductItem(pizza, restaurant2);
    productItemRepository.save(item1);
    productItemRepository.save(item2);

    List<ProductItemResponseDto> result =
        restaurantService.searchProductBy(restaurant1.getId(), null, null, null, null, null);

    assertEquals(2, result.size());
  }

  @Test
  void searchProductBy_shouldFilter_withMultipleFilters() {
    Product pizza =
        productRepository.save(new Product(12.50, "Delicious pizza", "Pizza", italianMenu));
    Product burger =
        productRepository.save(new Product(8.50, "Tasty burger", "Burger", italianMenu));
    italianMenu.addProduct(pizza);
    italianMenu.addProduct(burger);
    menuRepository.save(italianMenu);

    ProductItem pizzaItem = new ProductItem(pizza, restaurant1);
    ProductItem burgerItem = new ProductItem(burger, restaurant1);
    pizzaItem.setStatus(ProductStatus.AVAILABLE);
    burgerItem.setStatus(ProductStatus.AVAILABLE);
    productItemRepository.save(pizzaItem);
    productItemRepository.save(burgerItem);

    List<ProductItemResponseDto> result =
        restaurantService.searchProductBy(
            restaurant1.getId(), "10.00", null, "urger", "AVAILABLE", null);

    assertEquals(1, result.size());
    assertEquals("Burger", result.get(0).designation());
  }

  @Test
  void searchProductBy_shouldReturnEmpty_whenNoMatches() {
    List<ProductItemResponseDto> result =
        restaurantService.searchProductBy(
            restaurant1.getId(), null, null, "NonExistent", null, null);

    assertTrue(result.isEmpty());
  }
}
