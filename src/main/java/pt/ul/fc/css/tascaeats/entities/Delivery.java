package pt.ul.fc.css.tascaeats.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Embeddable
public class Delivery {

    /*@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)     estou a comentar os ids nas classes embebidas porque ta a dar barraca 
    private Long id;*/                                     //eu acho q elas n tem id proprio, mas sim o id da classe que as embebe

    @Column(nullable = false)
    private LocalDateTime pickedUpTimeStamp;

    private LocalDateTime deliveryTimeStamp;

    @ManyToOne
    @JoinColumn(name = "delivery_person_id", nullable = false)
    private DeliveryPerson deliveryPerson;

    protected Delivery() {}

    public Delivery(DeliveryPerson deliveryPerson) {
        this.pickedUpTimeStamp = LocalDateTime.now();
        this.deliveryPerson = deliveryPerson;
    }

    /*public Long getId() {
        return id;
    }*/

    public LocalDateTime getPickedUpTimeStamp() {
        return pickedUpTimeStamp;
    }

    public LocalDateTime getDeliveryTimeStamp() {
        return deliveryTimeStamp;
    }

    public void setDeliveryTimeStamp() {
        this.deliveryTimeStamp = LocalDateTime.now();
    }

    public DeliveryPerson getDeliveryMan() {
        return deliveryPerson;
    }

    public void setDeliveryMan(DeliveryPerson deliveryPerson) {
        this.deliveryPerson = deliveryPerson;
    }
}
