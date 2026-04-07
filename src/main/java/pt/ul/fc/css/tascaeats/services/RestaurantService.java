package pt.ul.fc.css.tascaeats.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import pt.ul.fc.css.tascaeats.dto.product.ProductRequest;
import pt.ul.fc.css.tascaeats.dto.product.ProductResponse;
import pt.ul.fc.css.tascaeats.dto.restaurant.RestaurantListResponse;
import pt.ul.fc.css.tascaeats.dto.restaurant.RestaurantRequest;
import pt.ul.fc.css.tascaeats.dto.restaurant.RestaurantResponse;
import pt.ul.fc.css.tascaeats.entities.Address;
import pt.ul.fc.css.tascaeats.entities.Admin;
import pt.ul.fc.css.tascaeats.entities.Product;
import pt.ul.fc.css.tascaeats.entities.Restaurant;
import pt.ul.fc.css.tascaeats.entities.enums.ProductStatus;
import pt.ul.fc.css.tascaeats.entities.enums.RestaurantStatus;
import pt.ul.fc.css.tascaeats.repository.ProductRepository;
import pt.ul.fc.css.tascaeats.repository.RestaurantRepository;
import pt.ul.fc.css.tascaeats.repository.UserRepository;

@Service
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    private RestaurantService(RestaurantRepository restaurantRepository, UserRepository userRepository, ProductRepository productRepository) {
        this.restaurantRepository = restaurantRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    /////////////////////////////////////////////////////////////
    ///  CRUD RESTAURANT'S METHODS
    /////////////////////////////////////////////////////////////
    
    public RestaurantListResponse searchByDesignationOrCity(String designation, String city) {
        if ((designation == null || designation.isEmpty()) && (city == null || city.isEmpty())) {
                return new RestaurantListResponse(restaurantRepository
                                                 .findAll()
                                                 .stream()
                                                 .map(this::mapRestaurantToRestaurantResponse)
                                                 .toList());
        }
        
        Optional<List<Restaurant>> rs;
        if (designation != null && city != null) {
            rs = restaurantRepository.findByDesignationContainingIgnoreCaseOrAddressCityContainingIgnoreCase(designation, city);

        }
        else if (city != null) {
            rs = restaurantRepository.findByAddressCityContainingIgnoreCase(city);
        }
        else {
            rs = restaurantRepository.findByDesignationContainingIgnoreCase(designation);
        }
        return new RestaurantListResponse(rs.map(rss -> 
                                                    rss.stream()
                                                    .map(this::mapRestaurantToRestaurantResponse)
                                                    .toList())
                                                    .orElse(null));
    }

    public RestaurantResponse createRestaurant(RestaurantRequest restaurant) {
        Address address = new Address(restaurant.getCity(), restaurant.getStreet(), restaurant.getPostalCode());
        Admin admin = userRepository.findById(restaurant.getAdminId())
                            .filter(a -> a instanceof Admin)
                            .map(a -> (Admin) a)
                            .orElseThrow(() -> new IllegalArgumentException("Admin with id " + restaurant.getAdminId() + " not found"));
        Restaurant r = new Restaurant(restaurant.getNif(), restaurant.getDesignation(), address, admin);
        
        admin.addRestaurant(r);
        Restaurant saved = restaurantRepository.save(r);
        return mapRestaurantToRestaurantResponse(saved);
    }

    public RestaurantResponse changeStatus(Long id, String status) {
        Optional<Restaurant> resOpt = restaurantRepository.findById(id);
        if(!resOpt.isPresent()){return null;}

        Restaurant restaurant = resOpt.get();
        restaurant.setStatus(RestaurantStatus.valueOf(status.toUpperCase()));
        Restaurant saved = restaurantRepository.save(restaurant);
        return mapRestaurantToRestaurantResponse(saved);
    }
    
    /////////////////////////////////////////////////////////////
    ///  CRUD PRODUCT'S METHODS
    /////////////////////////////////////////////////////////////

    public ProductResponse addProduct(Long restaurantId, ProductRequest productRequest) {
        Optional<Restaurant> resOpt = restaurantRepository.findById(restaurantId);
        if(!resOpt.isPresent()){return null;}

        Restaurant restaurant = resOpt.get();
        Product product = new Product(productRequest.getPrice(), productRequest.getDescription(), 
                                productRequest.getDesignation(), restaurant);
        
        Product saved = productRepository.save(product);
        
        return mapProductToProductResponse(saved);
    }

    public ProductResponse updateProduct(Long restaurantId, Long productId, ProductRequest productRequest) {        
        Optional<Product> prodOpt = productRepository.findById(productId);
        if(!prodOpt.isPresent()){return null;}

        Product product = prodOpt.get();
        product.setDescription(productRequest.getDescription());
        product.setDesignation(productRequest.getDesignation());
        product.setPrice(productRequest.getPrice());
        product.setStatus(ProductStatus.valueOf(productRequest.getAvailability().toUpperCase()));

        Product saved = productRepository.save(product);

        return mapProductToProductResponse(saved);
    }

    public boolean deleteProduct(Long restaurantId, Long productId){
        if(isFromRestaurant(productId, restaurantId) && !isAssociatedWithOrders(productId)){
            productRepository.deleteById(productId);
            return true;    
        }
        return false;
    }


    /////////////////////////////////////////////////////////////
    ///  HELPER PRIVATE  METHODS
    /////////////////////////////////////////////////////////////
    private boolean isAssociatedWithOrders(Long productId) {
        return false;
    }

    private boolean isFromRestaurant(Long productId, Long restaurandId){
        Optional<Product> prodOpt = productRepository.findById(productId);
        return prodOpt.get().getRestaurant().getId() == restaurandId;
    }

    /////////////////////////////////////////////////////////////
    ///  MAPPER METHODS
    /////////////////////////////////////////////////////////////
    private ProductResponse mapProductToProductResponse(Product product) {
        return new ProductResponse(
                                    product.getId(), 
                                    product.getPrice(), 
                                    product.getDescription(), 
                                    product.getDesignation(), 
                                    product.getStatus().toString());
    }

    private RestaurantResponse mapRestaurantToRestaurantResponse(Restaurant restaurant) {
        return new RestaurantResponse(
                                    restaurant.getId(),
                                    restaurant.getDesignation(),
                                    restaurant.getStatus().toString(),
                                    restaurant.getNif(),
                                    restaurant.getAdmin().getName(),
                                    restaurant.getAddress().getStreet(),
                                    restaurant.getAddress().getCity(),
                                    restaurant.getAddress().getPostalCode());
    }
    
}
