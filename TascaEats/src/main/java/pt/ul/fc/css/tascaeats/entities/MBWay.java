package pt.ul.fc.css.tascaeats.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class MBWay extends APayment {

  @Column(nullable = false)
  private String phoneNumber;

  protected MBWay() {}

  public MBWay(double amount, Order order, String phoneNumber) {
    super(amount, order);
    this.phoneNumber = phoneNumber;
  }

  public String getPhoneNumber() {
    return this.phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }
}
