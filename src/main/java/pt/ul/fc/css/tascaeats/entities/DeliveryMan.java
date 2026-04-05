package pt.ul.fc.css.tascaeats.entities;

import pt.ul.fc.css.tascaeats.entities.enums.DeliveryManStatus;

import jakarta.persistence.*;

@Entity
public class DeliveryMan extends User {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeliveryManStatus status;
    
    public DeliveryMan() {}

    public DeliveryMan(String userName, String name, String phoneNumber, String nif) {
        super(userName, name, phoneNumber, nif);
        this.status = DeliveryManStatus.AVAILABLE;
    }

    public DeliveryManStatus getDeliveryManStatus() {
        return status;
    }

    public void setDeliveryManStatus(DeliveryManStatus status) {
        this.status = status;
    }
}
