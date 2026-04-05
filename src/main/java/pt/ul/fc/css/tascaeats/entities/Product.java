package pt.ul.fc.css.tascaeats.entities;

import pt.ul.fc.css.tascaeats.entities.enums.ProductStatus;

import jakarta.persistence.*;

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

    public Product() {}

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
