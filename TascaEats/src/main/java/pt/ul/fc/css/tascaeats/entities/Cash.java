package pt.ul.fc.css.tascaeats.entities;

import jakarta.persistence.Entity;

@Entity
public class Cash extends APayment {

  // @Column(nullable = false)
  private double change;

  protected Cash() {}

  public Cash(double amount, Order order, double change) {
    super(amount, order);
    this.change = change;
  }

  public double getPhoneNumber() {
    return this.change;
  }

  public void setPhoneNumber(double change) {
    this.change = change;
  }
}
