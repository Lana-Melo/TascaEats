package pt.ul.fc.css.tascaeats.services;

import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import pt.ul.fc.css.tascaeats.dto.product.ProductRequestDto;
import pt.ul.fc.css.tascaeats.dto.product.ProductResponseDto;
import pt.ul.fc.css.tascaeats.dto.restaurant.RestaurantRequestDto;
import pt.ul.fc.css.tascaeats.dto.restaurant.RestaurantResponseDto;
import pt.ul.fc.css.tascaeats.dto.restaurant.ReviewRequestDto;
import pt.ul.fc.css.tascaeats.dto.restaurant.ReviewResponseDto;
import pt.ul.fc.css.tascaeats.entities.AUser;
import pt.ul.fc.css.tascaeats.entities.Address;
import pt.ul.fc.css.tascaeats.entities.Admin;
import pt.ul.fc.css.tascaeats.entities.Client;
import pt.ul.fc.css.tascaeats.entities.Menu;
import pt.ul.fc.css.tascaeats.entities.Order;
import pt.ul.fc.css.tascaeats.entities.Product;
import pt.ul.fc.css.tascaeats.entities.Restaurant;
import pt.ul.fc.css.tascaeats.entities.Review;
import pt.ul.fc.css.tascaeats.entities.enums.OrderStatus;
import pt.ul.fc.css.tascaeats.entities.enums.ProductStatus;
import pt.ul.fc.css.tascaeats.entities.enums.RestaurantStatus;
import pt.ul.fc.css.tascaeats.repository.MenuRepository;
import pt.ul.fc.css.tascaeats.repository.OrderRepository;
import pt.ul.fc.css.tascaeats.repository.ProductRepository;
import pt.ul.fc.css.tascaeats.repository.RestaurantRepository;
import pt.ul.fc.css.tascaeats.repository.UserRepository;

@Service
public class RestaurantService {

  private final RestaurantRepository restaurantRepository;
  private final ProductRepository productRepository;
  private final UserRepository userRepository;
  private final OrderRepository orderRepository;
  private final MenuRepository menuRepository;

  public RestaurantService(
      RestaurantRepository restaurantRepository,
      UserRepository userRepository,
      MenuRepository menuRepository,
      ProductRepository productRepository,
      OrderRepository orderRepository) {
    this.restaurantRepository = restaurantRepository;
    this.productRepository = productRepository;
    this.userRepository = userRepository;
    this.orderRepository = orderRepository;
    this.menuRepository = menuRepository;
  }

  /////////////////////////////////////////////////////////////
  ///  CRUD RESTAURANT'S METHODS
  /////////////////////////////////////////////////////////////

  public List<RestaurantResponseDto> searchByDesignationOrCity(String designation, String city) {
    if ((designation == null || designation.isEmpty()) && (city == null || city.isEmpty())) {
      return restaurantRepository.findAll().stream()
          .map(this::mapRestaurantToRestaurantResponse)
          .toList();
    }

    Optional<List<Restaurant>> rs;
    if (designation != null && city != null) {
      rs =
          restaurantRepository
              .findByDesignationContainingIgnoreCaseAndAddressCityContainingIgnoreCase(
                  designation, city);

    } else if (city != null) {
      rs = restaurantRepository.findByAddressCityContainingIgnoreCase(city);
    } else {
      rs = restaurantRepository.findByDesignationContainingIgnoreCase(designation);
    }
    return rs.map(list -> list.stream().map(this::mapRestaurantToRestaurantResponse).toList())
        .orElse(Collections.emptyList());
  }

  public List<RestaurantResponseDto> getAllRestaurants() {
    return restaurantRepository.findAll().stream()
        .map(this::mapRestaurantToRestaurantResponse)
        .toList();
  }

  public RestaurantResponseDto createRestaurant(RestaurantRequestDto restaurant) {
    Optional<AUser> userOpt = userRepository.findById(restaurant.getAdminId());
    if (userOpt.isEmpty() || !(userOpt.get() instanceof Admin)) {
      return null;
    }

    Admin admin = (Admin) userOpt.get();
    Address address =
        new Address(restaurant.getStreet(), restaurant.getCity(), restaurant.getPostalCode());
    Restaurant r = new Restaurant(restaurant.getNif(), restaurant.getDesignation(), address, admin);

    Restaurant saved = restaurantRepository.save(r);
    return mapRestaurantToRestaurantResponse(saved);
  }

  @Transactional
  public RestaurantResponseDto changeStatus(Long id, String status) {
    Optional<Restaurant> resOpt = restaurantRepository.findById(id);
    if (!resOpt.isPresent()) {
      return null;
    }

    Restaurant restaurant = resOpt.get();
    restaurant.setStatus(RestaurantStatus.valueOf(status.toUpperCase()));
    Restaurant saved = restaurantRepository.save(restaurant);
    return mapRestaurantToRestaurantResponse(saved);
  }

  @Transactional
  public RestaurantResponseDto assignMenu(Long id, Long menuId) {
    Optional<Restaurant> resOpt = restaurantRepository.findById(id);
    if (!resOpt.isPresent()) {
      return null;
    }
    Optional<Menu> menuOpt = menuRepository.findById(menuId);
    if (!menuOpt.isPresent()) {
      return null;
    }

    Restaurant restaurant = resOpt.get();
    Menu menu = menuOpt.get();
    restaurant.setMenu(menu);
    menu.addRestaurant(restaurant);

    menuRepository.save(menu);
    Restaurant saved = restaurantRepository.save(restaurant);
    return mapRestaurantToRestaurantResponse(saved);
  }

  /////////////////////////////////////////////////////////////
  ///  CRUD PRODUCT'S METHODS
  /////////////////////////////////////////////////////////////

  public ProductResponseDto addProduct(Long restaurantId, ProductRequestDto productRequest) {
    Optional<Restaurant> resOpt = restaurantRepository.findById(restaurantId);
    if (!resOpt.isPresent()) {
      return null;
    }

    Restaurant restaurant = resOpt.get();
    Product product =
        new Product(
            productRequest.getPrice(),
            productRequest.getDescription(),
            productRequest.getDesignation(),
            restaurant);

    Product saved = productRepository.save(product);

    return mapProductToProductResponse(saved);
  }

  @Transactional
  public ProductResponseDto updateProduct(
      Long restaurantId, Long productId, ProductRequestDto productRequest) {
    Optional<Product> prodOpt = productRepository.findById(productId);
    if (!prodOpt.isPresent()) {
      return null;
    }

    Product product = prodOpt.get();
    product.setDescription(productRequest.getDescription());
    product.setDesignation(productRequest.getDesignation());
    product.setPrice(productRequest.getPrice());
    product.setStatus(ProductStatus.valueOf(productRequest.getAvailability().toUpperCase()));

    Product saved = productRepository.save(product);

    return mapProductToProductResponse(saved);
  }

  @Transactional
  public boolean deleteProduct(Long restaurantId, Long productId) {
    boolean isFromRestaurant = productRepository.existsByIdAndRestaurantId(productId, restaurantId);

    if (isFromRestaurant) {
      if (!orderRepository.existsByProductsId(productId)) {
        productRepository.deleteById(productId); // definitively delete
      } else if (orderRepository.existsByProductsIdAndStatusNotIn(
          productId, List.of(OrderStatus.CANCELLED, OrderStatus.DELIVERED))) {
        return false; // don't delete product associated with orders in progress
      } else {
        Product product = productRepository.findById(productId).get();
        product.setDeletedAt(LocalDateTime.now()); // soft delete
        productRepository.save(product);
      }
      return true;
    }
    return false;
  }

  public List<ProductResponseDto> getProducts(Long restaurantId) {
    return productRepository
        .findAllByRestaurantId(restaurantId)
        .map(products -> products.stream().map(this::mapProductToProductResponse).toList())
        .orElse(Collections.emptyList());
  }

  /////////////////////////////////////////////////////////////
  ///  REVIEW METHODS
  /////////////////////////////////////////////////////////////
  public ReviewResponseDto addReview(Long restaurantId, ReviewRequestDto reviewRequest) {
    Optional<Restaurant> resOpt = restaurantRepository.findById(restaurantId);
    if (!resOpt.isPresent()) {
      return null;
    }
    Optional<AUser> userOpt = userRepository.findById(reviewRequest.getClientId());
    if (userOpt.isEmpty() || !(userOpt.get() instanceof Client)) {
      return null;
    }

    Restaurant restaurant = resOpt.get();
    Client client = (Client) userOpt.get();
    List<Order> orders = restaurant.getOrders();
    boolean canReview = false;

    for (Order order : orders) {
      if (order.getClient().getId() == client.getId()) {
        canReview = true;
      }
    }
    if (!canReview || reviewRequest.getRating() < 1 || reviewRequest.getRating() > 5) {
      return null;
    }

    Review review =
        new Review(reviewRequest.getRating(), reviewRequest.getComment(), client, restaurant);

    client.addReview(review);
    restaurant.addReview(review);
    userRepository.save(client);
    restaurantRepository.save(restaurant);

    return mapReviewToReviewResponse(review);
  }

  public List<ReviewResponseDto> getReviews(Long restaurantId) {
    Optional<Restaurant> resOpt = restaurantRepository.findById(restaurantId);
    if (!resOpt.isPresent()) {
      return null;
    }

    Restaurant restaurant = resOpt.get();
    List<Review> reviews = restaurant.getReviews();

    List<ReviewResponseDto> responses = new ArrayList<>();
    for (Review review : reviews) {
      responses.add(mapReviewToReviewResponse(review));
    }
    return responses;
  }

  /////////////////////////////////////////////////////////////
  ///  MAPPER METHODS
  /////////////////////////////////////////////////////////////
  private ProductResponseDto mapProductToProductResponse(Product product) {
    return new ProductResponseDto(
        product.getId(),
        product.getPrice(),
        product.getDescription(),
        product.getDesignation(),
        product.getStatus().toString());
  }

  private RestaurantResponseDto mapRestaurantToRestaurantResponse(Restaurant restaurant) {
    Long menuId = restaurant.getMenu() == null ? null : restaurant.getMenu().getId();
    return new RestaurantResponseDto(
        restaurant.getId(),
        restaurant.getDesignation(),
        restaurant.getStatus().toString(),
        restaurant.getNif(),
        restaurant.getAdmin().getName(),
        restaurant.getAddress().getStreet(),
        restaurant.getAddress().getCity(),
        restaurant.getAddress().getPostalCode(),
        menuId);
  }

  private ReviewResponseDto mapReviewToReviewResponse(Review review) {
    return new ReviewResponseDto(
        review.getId(),
        review.getRating(),
        review.getComment(),
        review.getClient().getId(),
        review.getRestaurant().getId());
  }
}
