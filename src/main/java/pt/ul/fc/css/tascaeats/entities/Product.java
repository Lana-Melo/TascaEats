package pt.ul.fc.css.tascaeats.entities;

import java.time.LocalDateTime;

import org.hibernate.annotations.Where;

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
@Where(clause = "deleted_at IS NULL") // For soft deletes
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt; // For soft deletes

    protected Product() {}

    public Product(double price, String description, String designation, Restaurant restaurant) {
        this.price = price;
        this.description = description;
        this.designation = designation;
        this.availability = ProductStatus.AVAILABLE;
        this.restaurant = restaurant;
    }

    public Long getId() {
        return id;
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

    public ProductStatus getStatus() {
        return availability;
    }

    public void setStatus (ProductStatus availability) {
        this.availability = availability;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant (Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public void setDeletedAt(LocalDateTime deletedAt){
        this.deletedAt = deletedAt;
    }

    public Object getDeletedAt() {
        return this.deletedAt;
    }
}
