package pt.ul.fc.css.tascaeats.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import pt.ul.fc.css.tascaeats.entities.enums.DeliveryPersonStatus;

@Entity
public class DeliveryPerson extends AUser {

    @Enumerated(EnumType.STRING)
    private DeliveryPersonStatus status;
    
    protected DeliveryPerson() {}

    public DeliveryPerson(String userName, String password, String name, String phoneNumber, String nif) {
        super(userName, password, name, phoneNumber, nif);
        this.status = DeliveryPersonStatus.AVAILABLE;
    }

    public DeliveryPersonStatus getDeliveryPersonStatus() {
        return status;
    }

    public void setDeliveryPersonStatus(DeliveryPersonStatus status) {
        this.status = status;
    }
}
