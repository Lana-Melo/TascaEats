package pt.ul.fc.css.tascaeats.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import pt.ul.fc.css.tascaeats.entities.enums.PaymentType;

@Entity
public class Payment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)    
    private Long id;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    private LocalDateTime paymentTimestamp;

    @Column(nullable = false)
    @Enumerated(jakarta.persistence.EnumType.STRING)
    private PaymentType method;

    // relation with order??
    // Constructors, getters, and setters
}
