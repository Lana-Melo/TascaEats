package pt.ul.fc.css.tascaeats.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import pt.ul.fc.css.tascaeats.dto.product.ProductRequest;
import pt.ul.fc.css.tascaeats.dto.product.ProductResponse;
import pt.ul.fc.css.tascaeats.dto.restaurant.RestaurantListResponse;
import pt.ul.fc.css.tascaeats.dto.restaurant.RestaurantRequest;
import pt.ul.fc.css.tascaeats.dto.restaurant.RestaurantResponse;
import pt.ul.fc.css.tascaeats.entities.enums.RestaurantStatus;
import pt.ul.fc.css.tascaeats.services.RestaurantService;

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantHandler;

    /**
     * GET    /api/restaurants              buscar por restaurante por nome ou cidade (caso de uso D)
     * POST   /api/restaurants              criar restaurante (caso de uso E)
     * PATCH  /api/restaurants/{id}/status   abrir/fechar restaurante (caso de uso E) (é só um campo por isso patch ao inves de put)
     * POST   /api/restaurants/{id}/products        adicionar prduto (caso de uso F)
     * GET    /api/restaurants/{id}/products        buscar produtos de um restaurante (caso de uso G)
     * PUT    /api/restaurants/{id}/products/{produtoid}  atualizar produto (caso de uso F)
     * DELETE /api/restaurants/{id}/products/{produtoid}  remover produto (caso de uso F)
     */

    //GET    /api/restaurants -> buscar por restaurante por nome ou cidade (caso de uso D)
    @GetMapping("/search")
    @ApiOperation(value = "Search restaurants", notes = "Returns a list of all restaurants filtered by name or city.")
    public ResponseEntity<RestaurantListResponse> searchRestaurant(@RequestParam(required = false) String designation, @RequestParam(required = false) String city) {
        RestaurantListResponse restaurants = restaurantHandler.searchByDesignationOrCity(designation, city); // chamo func searchByDesignationOrCity(designation, city) do RestaurantService / handler
        return restaurants != null ? ResponseEntity.ok(restaurants) : ResponseEntity.notFound().build();
    }

    //POST   /api/restaurants -> criar restaurante (caso de uso E)
    @PostMapping
    @ApiOperation(value = "Create restaurant", notes = "Creates a new restaurant.")
    public ResponseEntity<RestaurantResponse> createRestaurant(@RequestBody RestaurantRequest request) {
        RestaurantResponse restaurant = restaurantHandler.createRestaurant(request); // chamo func createRestaurant(request) do RestaurantService / handler
        return restaurant != null ? ResponseEntity.ok(restaurant) : ResponseEntity.badRequest().build();
    }

    //PATCH  /api/restaurants/{id}/status -> abrir/fechar restaurante (caso de uso E) (é só um campo por isso patch ao inves de put)
    @PatchMapping("/{id}/status")
    @ApiOperation(value = "Update restaurant status", notes = "Opens or closes a restaurant.")
    public ResponseEntity<RestaurantResponse> updateStatus(@PathVariable Long id, @RequestParam RestaurantStatus status) { // passar a string OPEN ou CLOSED
        RestaurantResponse restaurant = restaurantHandler.changeStatus(id, status.toString()); // chamo func changeStatus(id, status) do RestaurantService / handler (changeStatus(id, status))
        //evetualmente teria de ver se quem ta a tentar mudar o status é admin do restaurante mas n sei enfim 
        return restaurant != null ? ResponseEntity.ok(restaurant) : ResponseEntity.notFound().build();
    } 

    //POST   /api/restaurants/{id}/products -> adicionar produto (caso de uso F)
    @PostMapping("/{id}/products")
    @ApiOperation(value = "Add product to restaurant", notes = "Adds a new product")
    public ResponseEntity<ProductResponse> addProduct(@PathVariable Long id, @RequestBody ProductRequest request) {
        ProductResponse product = restaurantHandler.addProduct(id, request); // chamo func addProduct(id_Restaurant, request) do RestaurantService / handler (addProduct(id, request))
        //a ideia aqui seria criar o produto e associa lo ao restaurante 
        return product != null ? ResponseEntity.ok(product) : ResponseEntity.notFound().build();
    }
    //PUT    /api/restaurants/{id}/products/{produtoid} -> atualizar produto (caso de uso F)
    @PutMapping("/{id}/products/{productId}")
    @ApiOperation(value = "Update product", notes = "Updates an existing product")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long id, @PathVariable Long productId, @RequestBody ProductRequest request) {
        ProductResponse product = null; // chamo func updateProduct(id_Restaurant, id_Product, request) do RestaurantService / handler (updateProduct(id, productId, request))
        return product != null ? ResponseEntity.ok(product) : ResponseEntity.notFound().build();
    }

    //DELETE /api/restaurants/{id}/products/{produtoid} -> remover produto (caso de uso F)
    @DeleteMapping("/{id}/products/{productId}")
    @ApiOperation(value = "Delete product", notes = "Deletes an existing product")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id, @PathVariable Long productId) {
        // chamo func deleteProduct(id_Restaurant, id_Product) do RestaurantService / handler (deleteProduct(id, productId))
        return null;//restaurantHandler.deleteProduct(id, productId) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
    //GET    /api/restaurants/{id}/products -> buscar produtos de um restaurante (caso de uso G)
    @GetMapping("/{id}/products")
    @ApiOperation(value = "Get restaurant products", notes = "Returns a list of products for a given restaurant.")
    public ResponseEntity<List<ProductResponse>> getProducts(@PathVariable Long id) {  
        List<ProductResponse> products = null; // chamo func getProducts(id_Restaurant) do RestaurantService / handler (getProducts(id))
        return products != null ? ResponseEntity.ok(products) : ResponseEntity.notFound().build();
    }
}
