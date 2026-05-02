package pt.ul.fc.di.css.javafxtascaeats.model;

import java.util.ArrayList;
import java.util.List;

public class DataModel {

  private CurrentUser currentUser;
  private List<RestaurantModel> restaurants = new ArrayList<>();
  private RestaurantModel currentRestaurant;

  public CurrentUser getCurrentUser() {
    return currentUser;
  }

  public void setCurrentUser(CurrentUser user) {
    this.currentUser = user;
  }

  public List<RestaurantModel> getRestaurants() {
    return restaurants;
  }

  public void setRestaurants(List<RestaurantModel> restaurants) {
    this.restaurants = restaurants;
  }

  public RestaurantModel getCurrentRestaurant() {
    return currentRestaurant;
  }

  public void setCurrentRestaurant(RestaurantModel restaurant) {
    this.currentRestaurant = restaurant;
  }
}
