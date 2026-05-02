package pt.ul.fc.di.css.javafxtascaeats.model;

public class ProductModel {

  private long id;
  private double price;
  private String description;
  private String designation;
  private String menu;
  private String status;

  public ProductModel(
      long id, double price, String description, String designation, String menu, String status) {
    this.id = id;
    this.price = price;
    this.description = description;
    this.designation = designation;
    this.menu = menu;
    this.status = status;
  }

  public long getId() {
    return id;
  }

  public double getPrice() {
    return price;
  }

  public String getDescription() {
    return description;
  }

  public String getDesignation() {
    return designation;
  }

  public String getMenu() {
    return menu;
  }

  public String getStatus() {
    return status;
  }
}
