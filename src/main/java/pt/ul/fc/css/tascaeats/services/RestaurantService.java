package pt.ul.fc.css.tascaeats.services;

import org.springframework.stereotype.Service;
import pt.ul.fc.css.tascaeats.repository.RestaurantRepository;
import java.util.List;
import java.util.Optional;
import pt.ul.fc.css.tascaeats.dto.RestaurantListResponse;
import pt.ul.fc.css.tascaeats.dto.RestaurantResponse;
import pt.ul.fc.css.tascaeats.entities.Restaurant;
import pt.ul.fc.css.tascaeats.entities.Address;
import pt.ul.fc.css.tascaeats.entities.Admin;

@Service
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    public RestaurantService(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    // vou mudar dps para receber o dto do request
    public RestaurantListResponse getRestaurants(String designation, String city) {
        Optional<List<Restaurant>> restaurants = restaurantRepository.findByDesignationContainingOrAddressCityContaining(designation, city);
        return new RestaurantListResponse(restaurants.map(rs -> 
                                                        rs.stream()
                                                        .map(this::mapRestaurantToRestaurantResponse)
                                                        .toList())
                                                        .orElse(null));
    }

    
    // vou mudar dps para receber o dto do request
    /**
     * Creates a new restaurant with the provided data. 
     * The restaurant is associated with the admin mentioned in the restaurant parameter.
     * @param restaurant the restaurant data to create (should be a DTO in a real implementation)
     * @return a RestaurantResponse representing the created restaurant
     * 
     */
    public RestaurantResponse createRestaurant(Restaurant restaurant) {
        // assumir que é o dto e nao um Restaurant
        Address address = new Address(restaurant.getAddress().getCity(), restaurant.getAddress().getStreet(), restaurant.getAddress().getPostalCode());
        Admin admin = new Admin(); //mudar dto pra ter o id d user q vai ser admin do restaurant 
        Restaurant r = new Restaurant(restaurant.getNif(), restaurant.getDesignation(), address, admin);

        admin.getRestaurants().add(r);
        Restaurant saved = restaurantRepository.save(r);
        
        return mapRestaurantToRestaurantResponse(saved);
    }


    private RestaurantResponse mapRestaurantToRestaurantResponse(Restaurant restaurant) {
        /*RestaurantResponse response = new RestaurantResponse();
        response.setId(restaurant.getId());
        response.setDesignation(restaurant.getDesignation());
        response.setStatus(restaurant.getStatus().toString());
        response.setCity(restaurant.getAddress().getCity());
        return response;*/
        return new RestaurantResponse(
            restaurant.getId(),
            restaurant.getDesignation(),
            restaurant.getStatus().toString(),
            restaurant.getNif(),
            restaurant.getAddress().getCity()
        );
    }
    
}
