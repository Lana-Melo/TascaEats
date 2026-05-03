package pt.ul.fc.di.css.javafxtascaeats.model;

public class OrderProductItemModel {

  private final long id;
  private final String designation;
  private final double price;

  public OrderProductItemModel(long id, String designation, double price) {
    this.id = id;
    this.designation = designation;
    this.price = price;
  }

  public long getId() {
    return id;
  }

  public String getDesignation() {
    return designation;
  }

  public double getPrice() {
    return price;
  }
}
