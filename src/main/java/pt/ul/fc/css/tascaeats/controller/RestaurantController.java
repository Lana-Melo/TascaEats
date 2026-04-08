package pt.ul.fc.css.tascaeats.controller;

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
import io.swagger.annotations.ApiOperation;
import jakarta.validation.Valid;

import pt.ul.fc.css.tascaeats.dto.product.ProductRequest;
import pt.ul.fc.css.tascaeats.dto.product.ProductResponse;
import pt.ul.fc.css.tascaeats.dto.restaurant.RestaurantRequest;
import pt.ul.fc.css.tascaeats.dto.restaurant.RestaurantResponse;
import pt.ul.fc.css.tascaeats.entities.enums.RestaurantStatus;
import pt.ul.fc.css.tascaeats.services.RestaurantService;

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {

    private final RestaurantService restaurantHandler;

    public RestaurantController(RestaurantService restaurantService){
        this.restaurantHandler = restaurantService;
    }

    @GetMapping("/search")
    @ApiOperation(value = "Search restaurants", notes = "Returns a list of all restaurants filtered by name or city.")
    public ResponseEntity<List<RestaurantResponse>> searchRestaurant(@RequestParam(required = false) String designation, @RequestParam(required = false) String city) {
        List<RestaurantResponse> restaurants = restaurantHandler.searchByDesignationOrCity(designation, city); 
        return restaurants != null ? ResponseEntity.ok(restaurants) : ResponseEntity.notFound().build();
    }

    @PostMapping
    @ApiOperation(value = "Create restaurant", notes = "Creates a new restaurant.")
    public ResponseEntity<RestaurantResponse> createRestaurant(@RequestBody @Valid RestaurantRequest request) {
        RestaurantResponse restaurant = restaurantHandler.createRestaurant(request); 
        return restaurant != null ? ResponseEntity.ok(restaurant) : ResponseEntity.badRequest().build();
    }

    @PatchMapping("/{id}/status")
    @ApiOperation(value = "Update restaurant status", notes = "Opens or closes a restaurant.")
    public ResponseEntity<RestaurantResponse> updateStatus(@PathVariable Long id, @RequestParam RestaurantStatus status) { 
        RestaurantResponse restaurant = restaurantHandler.changeStatus(id, status.toString());
        return restaurant != null ? ResponseEntity.ok(restaurant) : ResponseEntity.notFound().build();
    } 

    @PostMapping("/{id}/products")
    @ApiOperation(value = "Add product to restaurant", notes = "Adds a new product")
    public ResponseEntity<ProductResponse> addProduct(@PathVariable Long id, @RequestBody @Valid ProductRequest request) {
        ProductResponse product = restaurantHandler.addProduct(id, request); 
        return product != null ? ResponseEntity.ok(product) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}/products/{productId}")
    @ApiOperation(value = "Update product", notes = "Updates an existing product")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long id, @PathVariable Long productId, @RequestBody @Valid ProductRequest request) {
        ProductResponse product = restaurantHandler.updateProduct(id, productId, request); 
        return product != null ? ResponseEntity.ok(product) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}/products/{productId}")
    @ApiOperation(value = "Delete product", notes = "Deletes an existing product")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id, @PathVariable Long productId) {
        return restaurantHandler.deleteProduct(id, productId) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/products")
    @ApiOperation(value = "Get restaurant products", notes = "Returns a list of products for a given restaurant.")
    public ResponseEntity<List<ProductResponse>> getProducts(@PathVariable Long id) {  
        List<ProductResponse> products = restaurantHandler.getProducts(id);
        return products != null ? ResponseEntity.ok(products) : ResponseEntity.notFound().build();
    }
}
