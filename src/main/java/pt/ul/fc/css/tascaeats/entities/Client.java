package pt.ul.fc.css.tascaeats.entities;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;

@Entity
public class Client extends AUser {
    
    @Embedded
    private Address address;

    public Client() {}

    public Client(String userName, String name, String phoneNumber, String nif, Address address) {
        super(userName, name, phoneNumber, nif);
        this.address = address;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
