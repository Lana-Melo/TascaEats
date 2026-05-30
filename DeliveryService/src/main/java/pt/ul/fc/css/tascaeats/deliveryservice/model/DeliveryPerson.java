package pt.ul.fc.css.tascaeats.deliveryservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.time.LocalDateTime;
import org.hibernate.annotations.Where;
import pt.ul.fc.css.tascaeats.deliveryservice.model.DeliveryPersonStatus;

@Entity
@Where(clause = "deleted_at IS NULL") // For soft deletes
public class DeliveryPerson extends AUser {

  @Enumerated(EnumType.STRING)
  private DeliveryPersonStatus status;

  @Column(name = "deleted_at")
  private LocalDateTime deletedAt; // For soft deletes

  protected DeliveryPerson() {}

  public DeliveryPerson(
      String username, String password, String name, String phoneNumber, String nif) {
    super(username, password, name, phoneNumber, nif);
    this.status = DeliveryPersonStatus.AVAILABLE;
  }

  public DeliveryPersonStatus getDeliveryPersonStatus() {
    return status;
  }

  public void setDeliveryPersonStatus(DeliveryPersonStatus status) {
    this.status = status;
  }

  public void setDeletedAt(LocalDateTime deletedAt) {
    this.deletedAt = deletedAt;
  }

  public LocalDateTime getDeletedAt() {
    return this.deletedAt;
  }
}
