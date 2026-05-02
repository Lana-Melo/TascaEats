package pt.ul.fc.css.tascaeats.controller;

import io.swagger.annotations.ApiOperation;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pt.ul.fc.css.tascaeats.dto.product.ProductItemResponseDto;
import pt.ul.fc.css.tascaeats.dto.restaurant.RestaurantRequestDto;
import pt.ul.fc.css.tascaeats.dto.restaurant.RestaurantResponseDto;
import pt.ul.fc.css.tascaeats.dto.restaurant.ReviewRequestDto;
import pt.ul.fc.css.tascaeats.dto.restaurant.ReviewResponseDto;
import pt.ul.fc.css.tascaeats.entities.enums.ProductStatus;
import pt.ul.fc.css.tascaeats.entities.enums.RestaurantStatus;
import pt.ul.fc.css.tascaeats.services.RestaurantService;

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {

  private final RestaurantService restaurantHandler;

  public RestaurantController(RestaurantService restaurantService) {
    this.restaurantHandler = restaurantService;
  }

  @GetMapping("/search")
  @ApiOperation(
      value = "Search restaurants",
      notes = "Returns a list of all restaurants filtered a given category.")
  public ResponseEntity<List<RestaurantResponseDto>> searchRestaurant(
      @RequestParam(required = false) String designation,
      @RequestParam(required = false) String city,
      @RequestParam(required = false) String street,
      @RequestParam(required = false) String postalCode,
      @RequestParam(required = false) String cuisine,
      @RequestParam(required = false) String orderNumber,
      @RequestParam(required = false) String reviewNumber,
      @RequestParam(required = false) String activeHour,
      @RequestParam(required = false) String average) {
    List<RestaurantResponseDto> restaurants =
        restaurantHandler.searchBy(
            designation,
            city,
            street,
            postalCode,
            cuisine,
            orderNumber,
            reviewNumber,
            activeHour,
            average);
    return ResponseEntity.ok(restaurants);
  }

  @GetMapping("/{id}/products/search")
  @ApiOperation(
      value = "Search products",
      notes = "Returns a list of all products filtered a given category.")
  public ResponseEntity<List<ProductItemResponseDto>> searchProduct(
      @RequestParam(required = false) Long id,
      @RequestParam(required = false) String designation,
      @RequestParam(required = false) String price,
      @RequestParam(required = false) String description,
      @RequestParam(required = false) String availability,
      @RequestParam(required = false) String timesOrdered) {
    List<ProductItemResponseDto> products =
        restaurantHandler.searchProductBy(
            id, price, designation, description, availability, timesOrdered);
    return ResponseEntity.ok(products);
  }

  @PostMapping
  @ApiOperation(value = "Create restaurant", notes = "Creates a new restaurant.")
  public ResponseEntity<RestaurantResponseDto> createRestaurant(
      @RequestBody @Valid RestaurantRequestDto request) {
    RestaurantResponseDto restaurant = restaurantHandler.createRestaurant(request);
    return ResponseEntity.ok(restaurant);
  }

  @PatchMapping("/{id}/status")
  @ApiOperation(value = "Update restaurant status", notes = "Opens or closes a restaurant.")
  public ResponseEntity<RestaurantResponseDto> updateStatus(
      @PathVariable Long id, @RequestParam RestaurantStatus status) {
    RestaurantResponseDto restaurant = restaurantHandler.changeStatus(id, status.toString());
    return ResponseEntity.ok(restaurant);
  }

  @PutMapping("/{id}/products/{productId}")
  @ApiOperation(value = "Update product status", notes = "Updates status of an existing product")
  public ResponseEntity<ProductItemResponseDto> updateProductStatus(
      @PathVariable Long id, @PathVariable Long productId, @RequestParam ProductStatus status) {
    ProductItemResponseDto product = restaurantHandler.updateProductStatus(id, productId, status);
    return ResponseEntity.ok(product);
  }

  @DeleteMapping("/{id}/products/{productId}")
  @ApiOperation(value = "Delete product", notes = "Deletes an existing product")
  public ResponseEntity<Void> deleteProductItem(
      @PathVariable Long id, @PathVariable Long productId) {
    return restaurantHandler.deleteProductItem(id, productId)
        ? ResponseEntity.noContent().build()
        : ResponseEntity.notFound().build();
  }

  @PostMapping("/{id}/reviews")
  @ApiOperation(value = "Create review", notes = "Adds a new review")
  public ResponseEntity<ReviewResponseDto> addReview(
      @PathVariable Long id, @RequestBody @Valid ReviewRequestDto request) {
    ReviewResponseDto review = restaurantHandler.addReview(id, request);
    return ResponseEntity.ok(review);
  }

  @GetMapping("/{id}/reviews")
  @ApiOperation(
      value = "Get restaurant reviews",
      notes = "Returns a list of products for a given restaurant.")
  public ResponseEntity<List<ReviewResponseDto>> getReviews(@PathVariable Long id) {
    List<ReviewResponseDto> review = restaurantHandler.getReviews(id);
    return ResponseEntity.ok(review);
  }

  @PatchMapping("/{id}/menu/{menuId}")
  @ApiOperation(value = "Assign menu", notes = "Assigns menu to restaurant given by id.")
  public ResponseEntity<RestaurantResponseDto> assignMenu(
      @PathVariable Long id, @PathVariable Long menuId) {
    RestaurantResponseDto restaurant = restaurantHandler.assignMenu(id, menuId);
    return ResponseEntity.ok(restaurant);
  }
}
