package pt.ul.fc.css.tascaeats.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import pt.ul.fc.css.tascaeats.entities.enums.PaymentType;

@Entity
public abstract class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private double amount;

    private LocalDateTime timestamp;

    @Column(nullable = false)
    private PaymentType type;

    @OneToOne
    private Order order;

    protected Payment() {}

    public Payment(String code, double amount, PaymentType type, Order order) {
        this.code = code;
        this.amount = amount;
        this.type = type;
        this.order = order;
    }

    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimeStamp() {
        this.timestamp = LocalDateTime.now();
    }

    public PaymentType getPaymentType() {
        return type;
    }

    public void setPaymentType(PaymentType type) {
        this.type = type;
    }

    public Order getOrder() {
        return order;
    }
}
