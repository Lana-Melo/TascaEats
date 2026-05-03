package pt.ul.fc.di.css.javafxtascaeats.model;

import java.util.List;

public class OrderModel {

  private long id;
  private String status;
  private double totalValue;
  private long clientId;
  private String clientName;
  private String clientNif;
  private String city;
  private String street;
  private String postalCode;
  private String createdAt;
  private String deliveryPersonName;
  private String deliveryPersonNif;
  private List<OrderProductItemModel> products;
  private double paymentAmount;
  private String paymentAt;
  private double paymentChange;

  public OrderModel(
      long id,
      String status,
      double totalValue,
      long clientId,
      String clientName,
      String clientNif,
      String city,
      String street,
      String postalCode,
      String createdAt,
      String deliveryPersonName,
      String deliveryPersonNif,
      List<OrderProductItemModel> products,
      double paymentAmount,
      String paymentAt,
      double paymentChange) {
    this.id = id;
    this.status = status;
    this.totalValue = totalValue;
    this.clientId = clientId;
    this.clientName = clientName;
    this.clientNif = clientNif;
    this.city = city;
    this.street = street;
    this.postalCode = postalCode;
    this.createdAt = createdAt;
    this.deliveryPersonName = deliveryPersonName;
    this.deliveryPersonNif = deliveryPersonNif;
    this.products = products;
    this.paymentAmount = paymentAmount;
    this.paymentAt = paymentAt;
    this.paymentChange = paymentChange;
  }

  public long getId() {
    return id;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
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

  public String getClientNif() {
    return clientNif;
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

  public String getCreatedAt() {
    return createdAt;
  }

  public String getDeliveryPersonName() {
    return deliveryPersonName;
  }

  public String getDeliveryPersonNif() {
    return deliveryPersonNif;
  }

  public List<OrderProductItemModel> getProducts() {
    return products;
  }

  public double getPaymentAmount() {
    return paymentAmount;
  }

  public String getPaymentAt() {
    return paymentAt;
  }

  public double getPaymentChange() {
    return paymentChange;
  }
}
