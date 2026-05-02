package pt.ul.fc.di.css.javafxtascaeats.model;

public class OrderModel {

  private long id;
  private String status;
  private double totalValue;
  private long clientId;
  private String clientName;
  private String city;
  private String street;
  private String postalCode;

  public OrderModel(
      long id,
      String status,
      double totalValue,
      long clientId,
      String clientName,
      String city,
      String street,
      String postalCode) {
    this.id = id;
    this.status = status;
    this.totalValue = totalValue;
    this.clientId = clientId;
    this.clientName = clientName;
    this.city = city;
    this.street = street;
    this.postalCode = postalCode;
  }

  public long getId() {
    return id;
  }

  public String getStatus() {
    return status;
  }

  public double getTotalValue() {
    return totalValue;
  }

  public long getClientId() {
    return clientId;
  }

  public String getClientName() {
    return clientName;
  }

  public String getCity() {
    return city;
  }

  public String getStreet() {
    return street;
  }

  public String getPostalCode() {
    return postalCode;
  }

  @Override
  public String toString() {
    return "Order #"
        + id
        + " | "
        + status
        + " | "
        + String.format("%.2f€", totalValue)
        + "\n 👤 "
        + clientName
        + "\n 📍 "
        + street
        + ", "
        + city
        + " "
        + postalCode;
  }
}
