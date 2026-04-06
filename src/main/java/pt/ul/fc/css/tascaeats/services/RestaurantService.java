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

    public RestaurantListResponse searchByDesignationOrCity(String designation, String city) {
        if (designation == null && city == null) { // se n for passado nenhum filtro, retorna todos os restaurantes
            return new RestaurantListResponse(restaurantRepository
                                                .findAll()
                                                .stream()
                                                .map(this::mapRestaurantToRestaurantResponse)
                                                .toList());
        }
        
        Optional<List<Restaurant>> rs;
        if (designation != null && city != null) {
            rs = restaurantRepository.findByDesignationContainingOrAddressCityContaining(designation, city);

        }
        else if (city != null) {
            rs = restaurantRepository.findByAddressCityContaining(city);
        }
        else {
            rs = restaurantRepository.findByDesignationContaining(designation);
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
    public RestaurantResponse createRestaurant(Restaurant restaurant) {
        // assumir que é o dto e nao um Restaurant
        Address address = new Address(restaurant.getAddress().getCity(), restaurant.getAddress().getStreet(), restaurant.getAddress().getPostalCode());
        Admin admin = new Admin();
        Restaurant r = new Restaurant(restaurant.getNif(), restaurant.getDesignation(), address, admin);

        admin.getRestaurants().add(r);
        Restaurant saved = restaurantRepository.save(r);
        
        return mapRestaurantToRestaurantResponse(saved);
    }

    private RestaurantResponse mapRestaurantToRestaurantResponse(Restaurant restaurant) {
        RestaurantResponse response = new RestaurantResponse();
        response.setId(restaurant.getId());
        response.setDesignation(restaurant.getDesignation());
        response.setStatus(restaurant.getStatus().toString());
        response.setCity(restaurant.getAddress().getCity());
        return response;
    }
    
}
