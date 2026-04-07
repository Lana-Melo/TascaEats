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

    public RestaurantListResponse searchByDesignationOrCity(String designation, String city) {
        if ((designation == null || designation.isEmpty()) && (city == null || city.isEmpty())) { // se n for passado nenhum filtro, retorna todos os restaurantes
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

    /**
     * Creates a new restaurant with the provided data. 
     * The restaurant is associated with the admin mentioned in the restaurant parameter.
     * @param restaurant the restaurant data to create (should be a DTO in a real implementation)
     * @return a RestaurantResponse representing the created restaurant
     * 
     */
    public RestaurantResponse createRestaurant(RestaurantRequest restaurant) {
        if(restaurant.getAdminId() == null) {
            throw new IllegalArgumentException("Admin ID must be provided to create a restaurant");
        }
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
        if(id == null || status == null || status.isEmpty()){
            throw new IllegalArgumentException("The given id or status is invalid");
        }

        Optional<Restaurant> resOpt = restaurantRepository.findById(id);
        if(!resOpt.isPresent()){return null;}

        Restaurant restaurant = resOpt.get();
        restaurant.setStatus(RestaurantStatus.valueOf(status.toUpperCase()));
        Restaurant saved = restaurantRepository.save(restaurant);
        return mapRestaurantToRestaurantResponse(saved);
    }

    public ProductResponse addProduct(Long restaurantId, ProductRequest productRequest) {
        if(restaurantId == null) {
            throw new IllegalArgumentException("Admin ID must be provided to create a restaurant");
        }

        Optional<Restaurant> resOpt = restaurantRepository.findById(restaurantId);
        if(!resOpt.isPresent()){return null;}

        Restaurant restaurant = resOpt.get();
        Product product = new Product(productRequest.getPrice(), productRequest.getDescription(), 
                                productRequest.getDesignation(), restaurant);
        
        Product saved = productRepository.save(product);
        
        return mapProductToProductResponse(saved); // id do product ta a vir null, preciso corrigir
    }

    private ProductResponse mapProductToProductResponse(Product product) {
        return new ProductResponse(
            product.getId(), 
            product.getPrice(), 
            product.getDescription(), 
            product.getDesignation(), 
            product.getStatus().toString()
        );
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
            restaurant.getAddress().getPostalCode()
        );
    }
    
}
