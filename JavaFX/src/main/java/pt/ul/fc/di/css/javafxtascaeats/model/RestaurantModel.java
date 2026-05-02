package pt.ul.fc.di.css.javafxtascaeats.model;

public class RestaurantModel {

  private long id;
  private String designation;
  private String status;
  private String nif;
  private String street;
  private String city;
  private String postalCode;
  private String openHours;
  private String closeHours;

  public RestaurantModel(
      long id,
      String designation,
      String status,
      String nif,
      String street,
      String city,
      String postalCode,
      String openHours,
      String closeHours) {
    this.id = id;
    this.designation = designation;
    this.status = status;
    this.nif = nif;
    this.street = street;
    this.city = city;
    this.postalCode = postalCode;
    this.openHours = openHours;
    this.closeHours = closeHours;
  }

  public long getId() {
    return id;
  }

  public String getDesignation() {
    return designation;
  }

  public String getStatus() {
    return status;
  }

  public String getNif() {
    return nif;
  }

  public String getStreet() {
    return street;
  }

  public String getCity() {
    return city;
  }

  public String getPostalCode() {
    return postalCode;
  }

  public String getOpenHours() {
    return openHours;
  }

  public String getCloseHours() {
    return closeHours;
  }

  @Override
  public String toString() {
    return designation.toUpperCase()
        + " | "
        + status
        + "\n 📍"
        + street
        + ", "
        + city
        + " "
        + postalCode
        + "\n 🕒"
        + openHours
        + " - "
        + closeHours;
  }
}
