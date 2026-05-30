package pt.ul.fc.css.tascaeats.deliveryservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "deliveries")
public class Delivery {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private DeliveryStatus status;

  @Column(nullable = false)
  private Long orderId;

  @Column(columnDefinition = "TIMESTAMP")
  private LocalDateTime pickedUpTimeStamp;

  @Column(columnDefinition = "TIMESTAMP")
  private LocalDateTime deliveryTimeStamp;

  @ManyToOne
  @JoinColumn(name = "delivery_person_id")
  private DeliveryPerson deliveryPerson;

  protected Delivery() {}

  public Delivery(LocalDateTime pickedUpTimeStamp, Long orderId) {
    this.pickedUpTimeStamp = pickedUpTimeStamp;
    this.orderId = orderId;
    this.status = DeliveryStatus.INCOMING;
  }

  public Long getId() {
    return id;
  }

  public Long getOrderId() {
    return orderId;
  }

  public LocalDateTime getPickedUpTimeStamp() {
    return pickedUpTimeStamp;
  }

  public LocalDateTime getDeliveryTimeStamp() {
    return deliveryTimeStamp;
  }

  public DeliveryPerson getDeliveryPerson() {
    return deliveryPerson;
  }

  public void setDeliveryPerson(DeliveryPerson deliveryPerson) {
    this.deliveryPerson = deliveryPerson;
  }

  public void setDeliveryTimeStamp() {
    this.deliveryTimeStamp = LocalDateTime.now();
  }

  public void setPickedUpTimeStamp() {
    this.pickedUpTimeStamp = LocalDateTime.now();
  }

  public DeliveryStatus getStatus() {
    return status;
  }

  public void setStatus(DeliveryStatus status) {
    this.status = status;
  }
}
