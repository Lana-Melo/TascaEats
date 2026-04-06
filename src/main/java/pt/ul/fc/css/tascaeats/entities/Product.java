package pt.ul.fc.css.tascaeats.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import pt.ul.fc.css.tascaeats.entities.enums.ProductStatus;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private String description;
    
    @Column(nullable = false)
    private String designation;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductStatus availability;

    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    protected Product() {}

    public Product(String code, double price, String description, String designation, Restaurant restaurant) {
        this.code = code;
        this.price = price;
        this.description = description;
        this.designation = designation;
        this.availability = ProductStatus.AVAILABLE;
        this.restaurant = restaurant;
    }

    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public ProductStatus getProductStatus() {
        return availability;
    }

    public void setProductStatus (ProductStatus availability) {
        this.availability = availability;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant (Restaurant restaurant) {
        this.restaurant = restaurant;
    }
}
