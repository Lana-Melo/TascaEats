package pt.ul.fc.css.tascaeats.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class Delivery {

    @Column(nullable = false)
    private LocalDateTime pickedUpTimestamp;

    private LocalDateTime deliveryTimestamp;

    protected Delivery() {}

    public Delivery(LocalDateTime pickedUpTimestamp) {
        this.pickedUpTimestamp = pickedUpTimestamp;
    }

    public LocalDateTime getPickedUpTimestamp() {
        return pickedUpTimestamp;
    }

    public void setPickedUpTimestamp(LocalDateTime pickedUpTimestamp) {
        this.pickedUpTimestamp = pickedUpTimestamp;
    }

    public LocalDateTime getDeliveryTimestamp() {
        return deliveryTimestamp;
    }

    public void setDeliveryTimeStamp() {
        this.deliveryTimestamp = LocalDateTime.now();
    }
}
