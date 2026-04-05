package pt.ul.fc.css.tascaeats.entities;

import pt.ul.fc.css.tascaeats.entities.enums.PaymentType;

import java.time.LocalDateTime;
import jakarta.persistence.*;

@Entity
public abstract class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private double amount;

    @Column()
    private LocalDateTime timestamp;

    @Column(nullable = false)
    private PaymentType type;

    public Payment() {}

    public Payment(String code, double amount, PaymentType type) {
        this.code = code;
        this.amount = amount;
        this.type = type;
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
}
