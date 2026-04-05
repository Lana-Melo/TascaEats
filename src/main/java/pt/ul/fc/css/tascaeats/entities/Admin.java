package pt.ul.fc.css.tascaeats.entities;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

@Entity
public class Admin extends AUser {
    
    @OneToMany(mappedBy = "admin", cascade = CascadeType.ALL)
    private List<Restaurant> restaurants;

    protected Admin() {}

    public List<Restaurant> getRestaurants() {
        return restaurants;
    }

    public void setRestaurants(List<Restaurant> restaurants) {
        this.restaurants = restaurants;
    }
}