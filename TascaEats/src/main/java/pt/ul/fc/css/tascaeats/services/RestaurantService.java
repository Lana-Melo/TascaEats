package pt.ul.fc.css.tascaeats.services;

import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import pt.ul.fc.css.tascaeats.dto.product.ProductItemResponseDto;
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
import pt.ul.fc.css.tascaeats.entities.ProductItem;
import pt.ul.fc.css.tascaeats.entities.Restaurant;
import pt.ul.fc.css.tascaeats.entities.Review;
import pt.ul.fc.css.tascaeats.entities.enums.OrderStatus;
import pt.ul.fc.css.tascaeats.entities.enums.ProductStatus;
import pt.ul.fc.css.tascaeats.entities.enums.RestaurantStatus;
import pt.ul.fc.css.tascaeats.exceptions.RequirementsNotMetException;
import pt.ul.fc.css.tascaeats.exceptions.ResourceNotFoundException;
import pt.ul.fc.css.tascaeats.repository.MenuRepository;
import pt.ul.fc.css.tascaeats.repository.OrderRepository;
import pt.ul.fc.css.tascaeats.repository.ProductItemRepository;
import pt.ul.fc.css.tascaeats.repository.RestaurantRepository;
import pt.ul.fc.css.tascaeats.repository.UserRepository;

@Service
public class RestaurantService {

  private final RestaurantRepository restaurantRepository;
  private final ProductItemRepository productItemRepository;
  private final UserRepository userRepository;
  private final OrderRepository orderRepository;
  private final MenuRepository menuRepository;

  public RestaurantService(
      RestaurantRepository restaurantRepository,
      UserRepository userRepository,
      MenuRepository menuRepository,
      ProductItemRepository productItemRepository,
      OrderRepository orderRepository) {
    this.restaurantRepository = restaurantRepository;
    this.productItemRepository = productItemRepository;
    this.userRepository = userRepository;
    this.orderRepository = orderRepository;
    this.menuRepository = menuRepository;
  }

  /////////////////////////////////////////////////////////////
  ///  CRUD RESTAURANT'S METHODS
  /////////////////////////////////////////////////////////////

  public List<RestaurantResponseDto> searchBy(
      String designation,
      String city,
      String street,
      String postalCode,
      String cuisine,
      String orderNumber,
      String reviewNumber,
      String activeHour,
      String average) {

    List<Restaurant> restaurants =
        (designation == null || designation.isEmpty())
            ? restaurantRepository.findAll()
            : restaurantRepository.findByDesignationContainingIgnoreCase(designation);

    if (city != null && !city.isEmpty()) {
      restaurants.retainAll(restaurantRepository.findByAddressCityContainingIgnoreCase(city));
    }
    if (street != null && !street.isEmpty()) {
      restaurants.retainAll(restaurantRepository.findByAddressStreetContainingIgnoreCase(street));
    }
    if (postalCode != null && !postalCode.isEmpty()) {
      restaurants.retainAll(
          restaurantRepository.findByAddressPostalCodeContainingIgnoreCase(postalCode));
    }
    if (cuisine != null && !cuisine.isEmpty()) {
      restaurants.retainAll(restaurantRepository.findByMenuCuisineContainingIgnoreCase(cuisine));
    }
    if (orderNumber != null && !orderNumber.isEmpty()) {
      restaurants.retainAll(restaurantRepository.findByOrderNumber(Integer.parseInt(orderNumber)));
    }
    if (reviewNumber != null && !reviewNumber.isEmpty()) {
      restaurants.retainAll(
          restaurantRepository.findByReviewNumber(Integer.parseInt(reviewNumber)));
    }
    if (activeHour != null && !activeHour.isEmpty()) {
      String[] hour = activeHour.split(":");
      restaurants.retainAll(
          restaurantRepository.findByActiveHours(
              LocalTime.of(Integer.parseInt(hour[0]), Integer.parseInt(hour[1]))));
    }
    if (average != null && !average.isEmpty()) {
      restaurants.retainAll(restaurantRepository.findByAveragePrice(Double.parseDouble(average)));
    }

    return restaurants.stream().map(this::mapRestaurantToRestaurantResponse).toList();
  }

  public RestaurantResponseDto createRestaurant(RestaurantRequestDto restaurant) {
    Optional<AUser> userOpt = userRepository.findById(restaurant.getAdminId());
    if (userOpt.isEmpty() || !(userOpt.get() instanceof Admin)) {
      throw new ResourceNotFoundException("User not found");
    }

    String[] open = restaurant.getOpenHours().split(":");
    String[] close = restaurant.getCloseHours().split(":");
    LocalTime openHours = LocalTime.of(Integer.parseInt(open[0]), Integer.parseInt(open[1]));
    LocalTime closeHours = LocalTime.of(Integer.parseInt(close[0]), Integer.parseInt(close[1]));

    Admin admin = (Admin) userOpt.get();
    Address address =
        new Address(restaurant.getStreet(), restaurant.getCity(), restaurant.getPostalCode());
    Restaurant r =
        new Restaurant(
            restaurant.getNif(),
            restaurant.getDesignation(),
            address,
            admin,
            openHours,
            closeHours);

    Restaurant saved = restaurantRepository.save(r);
    return mapRestaurantToRestaurantResponse(saved);
  }

  @Transactional
  public RestaurantResponseDto changeStatus(Long id, String status) {
    Optional<Restaurant> resOpt = restaurantRepository.findById(id);
    if (!resOpt.isPresent()) {
      throw new ResourceNotFoundException("Restaurant not found");
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
      throw new ResourceNotFoundException("Restaurant not found");
    }
    Optional<Menu> menuOpt = menuRepository.findById(menuId);
    if (!menuOpt.isPresent()) {
      throw new ResourceNotFoundException("Menu not found");
    }

    Restaurant restaurant = resOpt.get();
    Menu menu = menuOpt.get();
    restaurant.setMenu(menu);
    menu.addRestaurant(restaurant);

    menuRepository.save(menu);

    for (ProductItem productItem : restaurant.getProductItems()) {
      deleteProductItem(restaurant.getId(), productItem.getId());
    }
    restaurant.setProductItems(new ArrayList<>());
    for (Product product : menu.getProducts()) {
      restaurant.addProductItem(new ProductItem(product, restaurant));
    }

    Restaurant saved = restaurantRepository.save(restaurant);
    return mapRestaurantToRestaurantResponse(saved);
  }

  /////////////////////////////////////////////////////////////
  ///  CRUD PRODUCT'S METHODS
  /////////////////////////////////////////////////////////////

  @Transactional
  public ProductItemResponseDto updateProductStatus(
      Long restaurantId, Long productId, ProductStatus status) {
    Optional<ProductItem> prodOpt = productItemRepository.findById(productId);
    if (!prodOpt.isPresent()) {
      throw new ResourceNotFoundException("Product not found");
    }

    ProductItem productItem = prodOpt.get();
    productItem.setStatus(status);

    ProductItem saved = productItemRepository.save(productItem);

    return mapProductItemToProductItemResponse(saved);
  }

  @Transactional
  public boolean deleteProductItem(Long restaurantId, Long productId) {
    boolean isFromRestaurant =
        productItemRepository.existsByIdAndRestaurantId(productId, restaurantId);

    if (isFromRestaurant) {
      if (!orderRepository.existsByProductItemsId(productId)) {
        productItemRepository.deleteById(productId); // definitively delete
      } else if (orderRepository.existsByProductItemsIdAndStatusNotIn(
          productId, List.of(OrderStatus.CANCELLED, OrderStatus.DELIVERED))) {
        return false; // don't delete product associated with orders in progress
      } else {
        ProductItem productItem = productItemRepository.findById(productId).get();
        productItem.setDeletedAt(LocalDateTime.now()); // soft delete
        productItemRepository.save(productItem);
      }
      return true;
    }
    return false;
  }

  public List<ProductItemResponseDto> searchProductBy(
      Long restaurantId,
      String price,
      String designation,
      String description,
      String availability,
      String timesOrdered) {
    List<ProductItem> productItems =
        (designation == null || designation.isEmpty())
            ? productItemRepository.findAll()
            : productItemRepository.findByProductDesignationContainingIgnoreCase(designation);

    if (description != null && !description.isEmpty()) {
      productItems.retainAll(
          productItemRepository.findByProductDescriptionContainingIgnoreCase(description));
    }
    if (price != null && !price.isEmpty()) {
      productItems.retainAll(
          productItemRepository.findByProductPriceLessThanEqual(Double.parseDouble(price)));
    }
    if (availability != null && !availability.isEmpty()) {
      productItems.retainAll(
          productItemRepository.findByAvailability(ProductStatus.valueOf(availability)));
    }
    if (timesOrdered != null && !timesOrdered.isEmpty()) {
      productItems.retainAll(
          productItemRepository.findByTimesOrdered(Integer.parseInt(timesOrdered)));
    }
    if (restaurantId != null) {
      productItems.retainAll(productItemRepository.findAllByRestaurantId(restaurantId));
    }
    return productItems.stream().map(this::mapProductItemToProductItemResponse).toList();
  }

  /////////////////////////////////////////////////////////////
  ///  REVIEW METHODS
  /////////////////////////////////////////////////////////////
  public ReviewResponseDto addReview(Long restaurantId, ReviewRequestDto reviewRequest) {
    Optional<Restaurant> resOpt = restaurantRepository.findById(restaurantId);
    if (!resOpt.isPresent()) {
      throw new ResourceNotFoundException("Restaurant not found");
    }
    Optional<AUser> userOpt = userRepository.findById(reviewRequest.getClientId());
    if (userOpt.isEmpty() || !(userOpt.get() instanceof Client)) {
      throw new ResourceNotFoundException("User not found");
    }

    Restaurant restaurant = resOpt.get();
    Client client = (Client) userOpt.get();
    List<Order> orders = restaurant.getOrders();
    boolean canReview = false;

    for (Order order : orders) {
      if (order.getClient().getId().equals(client.getId())) {
        canReview = true;
      }
    }
    if (!canReview) {
      throw new RequirementsNotMetException("You cannot review this restaurant");
    }
    if (reviewRequest.getRating() < 1 || reviewRequest.getRating() > 5) {
      throw new RequirementsNotMetException("Rating must be between 1 and 5");
    }

    Review review =
        new Review(reviewRequest.getRating(), reviewRequest.getComment(), client, restaurant);

    // client.addReview(review);
    restaurant.addReview(review);
    // userRepository.save(client);
    restaurantRepository.save(restaurant);

    return mapReviewToReviewResponse(review);
  }

  public List<ReviewResponseDto> getReviews(Long restaurantId) {
    Optional<Restaurant> resOpt = restaurantRepository.findById(restaurantId);
    if (!resOpt.isPresent()) {
      throw new ResourceNotFoundException("Restaurant not found");
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
  private ProductItemResponseDto mapProductItemToProductItemResponse(ProductItem productItem) {
    return new ProductItemResponseDto(
        productItem.getId(),
        productItem.getProduct().getId(),
        productItem.getRestaurant().getId(),
        productItem.getProduct().getDesignation(),
        productItem.getProduct().getDescription(),
        productItem.getStatus().toString(),
        productItem.getProduct().getPrice());
  }

  private RestaurantResponseDto mapRestaurantToRestaurantResponse(Restaurant restaurant) {
    Long menuId = restaurant.getMenu() == null ? null : restaurant.getMenu().getId();
    Double av = restaurant.getReviews().stream().mapToInt(Review::getRating).average().orElse(0.0);
    double roundedAv = Math.round(av * 100.0) / 100.0;
    return new RestaurantResponseDto(
        restaurant.getId(),
        restaurant.getDesignation(),
        restaurant.getStatus().toString(),
        restaurant.getNif(),
        restaurant.getAdmin().getName(),
        restaurant.getAddress().getStreet(),
        restaurant.getAddress().getCity(),
        restaurant.getAddress().getPostalCode(),
        restaurant.getOpenHours().toString(),
        restaurant.getCloseHours().toString(),
        menuId,
        roundedAv,
        restaurant.getReviews().size());
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
