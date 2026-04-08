package pt.ul.fc.css.tascaeats.entities;

import jakarta.persistence.Embeddable;

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
}
