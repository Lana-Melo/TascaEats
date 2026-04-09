package pt.ul.fc.css.tascaeats.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

@Entity
public class Admin extends AUser {
    
    @OneToMany(mappedBy = "admin", cascade = CascadeType.ALL)
    private List<Restaurant> restaurants;

    public Admin() {}

    public Admin(String username, String password, String name, String phoneNumber, String nif) {
        super(username, password, name, phoneNumber, nif);
        this.restaurants = new ArrayList<Restaurant>();
    }

    public List<Restaurant> getRestaurants() {
        return restaurants;
    }

    public void setRestaurants(List<Restaurant> restaurants) {
        this.restaurants = restaurants;
    }

    public void addRestaurant(Restaurant restaurant) {
        this.restaurants.add(restaurant);
    }
}