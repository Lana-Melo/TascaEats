package pt.ul.fc.css.tascaeats.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import pt.ul.fc.css.tascaeats.entities.enums.RestaurantStatus;

@Entity
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String designation;

    @Column(nullable = false)
    private RestaurantStatus status;

    @Column(nullable = false)
    private String nif;

    // relation with orders, admin and products??
    // Constructors, getters, and setters
}
