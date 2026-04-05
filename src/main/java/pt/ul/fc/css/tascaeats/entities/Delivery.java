package pt.ul.fc.css.tascaeats.entities;

import java.time.LocalDateTime;
import jakarta.persistence.*;

@Embeddable
public class Delivery {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "picked_up_time", nullable = false)
    private LocalDateTime pickedUpTimeStamp;

    @Column(name = "delivered_time")
    private LocalDateTime deliveryTimeStamp;

    @ManyToOne
    @JoinColumn(name = "delivery_man_id", nullable = false)
    private DeliveryMan deliveryMan;

    public Delivery() {}

    public Delivery(DeliveryMan deliveryMan) {
        this.pickedUpTimeStamp = LocalDateTime.now();
        this.deliveryMan = deliveryMan;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getPickedUpTimeStamp() {
        return pickedUpTimeStamp;
    }

    public LocalDateTime getDeliveryTimeStamp() {
        return deliveryTimeStamp;
    }

    public void setDeliveryTimeStamp() {
        this.deliveryTimeStamp = LocalDateTime.now();
    }

    public DeliveryMan getDeliveryMan() {
        return deliveryMan;
    }

    public void setDeliveryMan(DeliveryMan deliveryMan) {
        this.deliveryMan = deliveryMan;
    }
}
