package pt.ul.fc.css.tascaeats.delivery.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;
import org.hibernate.annotations.Where;

@Entity
@Where(clause = "deleted_at IS NULL") // For soft deletes
public class DeliveryPerson {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true, nullable = false)
  private String username;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false)
  private String name;

  @Column(unique = true, nullable = false)
  private String phoneNumber;

  @Column(unique = true, nullable = false)
  private String nif;

  @Enumerated(EnumType.STRING)
  private DeliveryPersonStatus status;

  @Column(name = "deleted_at")
  private LocalDateTime deletedAt; // For soft deletes

  protected DeliveryPerson() {}

  public DeliveryPerson(
      String username, String password, String name, String phoneNumber, String nif) {
    this.username = username;
    this.password = password;
    this.name = name;
    this.phoneNumber = phoneNumber;
    this.nif = nif;
    this.status = DeliveryPersonStatus.AVAILABLE;
  }

  public Long getId() {
    return id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public String getNif() {
    return nif;
  }

  public void setNif(String nif) {
    this.nif = nif;
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
