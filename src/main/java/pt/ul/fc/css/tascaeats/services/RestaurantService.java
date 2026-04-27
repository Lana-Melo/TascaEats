package pt.ul.fc.css.tascaeats.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import pt.ul.fc.css.tascaeats.dto.product.ProductRequest;
import pt.ul.fc.css.tascaeats.dto.product.ProductResponse;
import pt.ul.fc.css.tascaeats.dto.restaurant.RestaurantRequest;
import pt.ul.fc.css.tascaeats.dto.restaurant.RestaurantResponse;
import pt.ul.fc.css.tascaeats.dto.restaurant.ReviewRequest;
import pt.ul.fc.css.tascaeats.dto.restaurant.ReviewResponse;
import pt.ul.fc.css.tascaeats.entities.AUser;
import pt.ul.fc.css.tascaeats.entities.Address;
import pt.ul.fc.css.tascaeats.entities.Admin;
import pt.ul.fc.css.tascaeats.entities.Client;
import pt.ul.fc.css.tascaeats.entities.Order;
import pt.ul.fc.css.tascaeats.entities.Product;
import pt.ul.fc.css.tascaeats.entities.Restaurant;
import pt.ul.fc.css.tascaeats.entities.Review;
import pt.ul.fc.css.tascaeats.entities.enums.OrderStatus;
import pt.ul.fc.css.tascaeats.entities.enums.ProductStatus;
import pt.ul.fc.css.tascaeats.entities.enums.RestaurantStatus;
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

    public RestaurantService(RestaurantRepository restaurantRepository, UserRepository userRepository, 
                            ProductRepository productRepository, OrderRepository orderRepository) {
        this.restaurantRepository = restaurantRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
    }

    /////////////////////////////////////////////////////////////
    ///  CRUD RESTAURANT'S METHODS
    /////////////////////////////////////////////////////////////
    
    public List<RestaurantResponse> searchByDesignationOrCity(String designation, String city) {
        if ((designation == null || designation.isEmpty()) && (city == null || city.isEmpty())) {
                return restaurantRepository.findAll()
                                           .stream()
                                           .map(this::mapRestaurantToRestaurantResponse)
                                           .toList();
        }
        
        Optional<List<Restaurant>> rs;
        if (designation != null && city != null) {
            rs = restaurantRepository.findByDesignationContainingIgnoreCaseAndAddressCityContainingIgnoreCase(designation, city);

        }
        else if (city != null) {
            rs = restaurantRepository.findByAddressCityContainingIgnoreCase(city);
        }
        else {
            rs = restaurantRepository.findByDesignationContainingIgnoreCase(designation);
        }
        return rs.map(list -> list.stream()
                .map(this::mapRestaurantToRestaurantResponse)
                .toList())
                .orElse(Collections.emptyList());
    }

    public RestaurantResponse createRestaurant(RestaurantRequest restaurant) {
        Optional<AUser> userOpt = userRepository.findById(restaurant.getAdminId());
        if (userOpt.isEmpty() || !(userOpt.get() instanceof Admin)) {return null;}

        Admin admin = (Admin) userOpt.get();
        Address address = new Address(restaurant.getStreet(), restaurant.getCity(), restaurant.getPostalCode());
        Restaurant r = new Restaurant(restaurant.getNif(), restaurant.getDesignation(), address, admin);
        
        Restaurant saved = restaurantRepository.save(r);
        return mapRestaurantToRestaurantResponse(saved);
    }

    @Transactional
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

    @Transactional
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

    @Transactional
    public boolean deleteProduct(Long restaurantId, Long productId){
        boolean isFromRestaurant = productRepository.existsByIdAndRestaurantId(productId, restaurantId);
        
        if(isFromRestaurant){
            if(!orderRepository.existsByProductsId(productId)){ 
                productRepository.deleteById(productId); // definitively delete
            } else if(orderRepository.existsByProductsIdAndStatusNotIn(productId, List.of(OrderStatus.CANCELLED, OrderStatus.DELIVERED))){
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

    public List<ProductResponse> getProducts(Long restaurantId) {
        return productRepository.findAllByRestaurantId(restaurantId)
                                    .map(products -> products.stream()
                                    .map(this::mapProductToProductResponse)
                                    .toList()).orElse(Collections.emptyList());
    }

    /////////////////////////////////////////////////////////////
    ///  CRUD REVIEW'S METHODS
    /////////////////////////////////////////////////////////////
    public ReviewResponse addReview(Long restaurantId, ReviewRequest reviewRequest) {
        Optional<Restaurant> resOpt = restaurantRepository.findById(restaurantId);
        if(!resOpt.isPresent()){return null;}
        Optional<AUser> userOpt = userRepository.findById(reviewRequest.getClientId());
        if(userOpt.isEmpty() || !(userOpt.get() instanceof Client)){return null;}

        Restaurant restaurant = resOpt.get();
        Client client = (Client) userOpt.get();
        List<Order> orders = restaurant.getOrders();
        boolean canReview = false;

        for (Order order : orders) {
            if (order.getClient().getId() == client.getId()) {
                canReview = true;
            }
        }
        if (!canReview || reviewRequest.getRating() < 1 || reviewRequest.getRating() > 5) {return null;}

        Review review = new Review(reviewRequest.getRating(), reviewRequest.getComment(), client, restaurant);
        
        client.addReview(review);
        restaurant.addReview(review);
        userRepository.save(client);
        restaurantRepository.save(restaurant);
        
        return mapReviewToReviewResponse(review);
    }

    public List<ReviewResponse> getReviews(Long restaurantId) {
        Optional<Restaurant> resOpt = restaurantRepository.findById(restaurantId);
        if(!resOpt.isPresent()){return null;}

        Restaurant restaurant = resOpt.get();
        List<Review> reviews = restaurant.getReviews();

        List<ReviewResponse> responses = new ArrayList<>();
        for (Review review : reviews) {
            responses.add(mapReviewToReviewResponse(review));
        }
        return responses;
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

    private ReviewResponse mapReviewToReviewResponse(Review review) {
        return new ReviewResponse(
                                    review.getId(), 
                                    review.getRating(), 
                                    review.getComment(), 
                                    review.getClient().getId(), 
                                    review.getRestaurant().getId());
    }
    
}
