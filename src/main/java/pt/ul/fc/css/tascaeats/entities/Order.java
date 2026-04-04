package pt.ul.fc.css.tascaeats.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import pt.ul.fc.css.tascaeats.entities.enums.OrderStatus;

@Entity
public class Order {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private LocalDateTime orderTimestamp;

    @Column
    private Double totalPrice;

    @Column
    @Enumerated(jakarta.persistence.EnumType.STRING)
    private OrderStatus status;

    @Embedded
    private Delivery delivery;

    // relation with restaurant, delivery person, customer and products??
    // Constructors, getters, and setters
}