package pt.ul.fc.css.tascaeats.entities;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;

@Entity
public class Client extends AUser {
    
    @Embedded
    private Address address;

    protected Client() {}

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
