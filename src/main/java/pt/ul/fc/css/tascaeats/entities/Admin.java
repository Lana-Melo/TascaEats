package pt.ul.fc.css.tascaeats.entities;

import java.util.List;
import java.util.ArrayList;
import jakarta.persistence.*;

@Entity
public class Admin extends User {
    
    @OneToMany(mappedBy = "admin", cascade = CascadeType.ALL)
    private List<Restaurant> restaurants;

    public Admin() {}

    public Admin(String userName, String name, String phoneNumber, String nif) {
        super(userName, name, phoneNumber, nif);
        this.restaurants = new ArrayList<Restaurant>();
    }

    public List<Restaurant> getRestaurants() {
        return restaurants;
    }

    public void setRestaurants(List<Restaurant> restaurants) {
        this.restaurants = restaurants;
    }
}
