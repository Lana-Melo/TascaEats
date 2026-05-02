package pt.ul.fc.css.tascaeats.entities;

import jakarta.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Address {

  private String street;

  private String city;

  private String postalCode;

  protected Address() {}

  public Address(String street, String city, String postalCode) {
    this.street = street;
    this.city = city;
    this.postalCode = postalCode;
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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Address)) return false;
    Address address = (Address) o;
    return Objects.equals(street, address.street)
        && Objects.equals(city, address.city)
        && Objects.equals(postalCode, address.postalCode);
  }

  @Override
  public int hashCode() {
    return Objects.hash(street, city, postalCode);
  }
}
