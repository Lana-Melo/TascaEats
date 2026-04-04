package pt.ul.fc.css.tascaeats.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import pt.ul.fc.css.tascaeats.entities.enums.ProductStatus;

@Entity
public class Product {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)    
    private Long id;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private String designation;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    @Enumerated(jakarta.persistence.EnumType.STRING)
    private ProductStatus status;

    // relation with restaurant, orders??
    // Constructors, getters, and setters   

}
