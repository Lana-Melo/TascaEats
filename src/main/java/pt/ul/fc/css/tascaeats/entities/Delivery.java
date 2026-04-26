package pt.ul.fc.css.tascaeats.entities;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;


@Embeddable
public class Delivery {
    
    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime pickedUpTimeStamp;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime deliveryTimeStamp;

    protected Delivery() {}

    public Delivery(LocalDateTime pickedUpTimeStamp) {
        this.pickedUpTimeStamp = pickedUpTimeStamp;
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

    public void setPickedUpTimeStamp() {
        this.pickedUpTimeStamp = LocalDateTime.now();
    }
}
