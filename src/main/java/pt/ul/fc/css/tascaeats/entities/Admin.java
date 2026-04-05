package pt.ul.fc.css.tascaeats.entities;

import java.util.List;
import java.util.ArrayList;
import jakarta.persistence.*;

@Entity
public class Admin extends AUser {
    
    @OneToMany(mappedBy = "admin", cascade = CascadeType.ALL)
    private List<Restaurant> restaurants;

    public Admin() {}

    public List<Restaurant> getRestaurants() {
        return restaurants;
    }

    public void setRestaurants(List<Restaurant> restaurants) {
        this.restaurants = restaurants;
    }
}