package pt.ul.fc.css.tascaeats.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Embeddable;

@Embeddable
public class Delivery {
    private LocalDateTime pickedUpTimestamp;
    private LocalDateTime deliveredTimestamp;
}
