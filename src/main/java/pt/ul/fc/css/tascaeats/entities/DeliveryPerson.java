package pt.ul.fc.css.tascaeats.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import pt.ul.fc.css.tascaeats.entities.enums.DeliveryPersonStatus;

@Entity
public class DeliveryPerson extends AUser {

    @Enumerated(jakarta.persistence.EnumType.STRING)
    private DeliveryPersonStatus status;

    // relation with orders??
    // Constructors, getters, and setters
}
