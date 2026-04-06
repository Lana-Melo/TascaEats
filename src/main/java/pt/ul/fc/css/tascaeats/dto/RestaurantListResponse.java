package pt.ul.fc.css.tascaeats.dto;

import java.util.List;

public class RestaurantListResponse {
    List<RestaurantResponse> restaurants;

    public RestaurantListResponse(List<RestaurantResponse> restaurants){
        this.restaurants = restaurants;
    }


}

