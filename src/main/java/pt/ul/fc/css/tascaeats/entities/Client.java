package pt.ul.fc.css.tascaeats.entities;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;

@Entity
public class Client extends AUser {
    
    @Embedded
    private Address address;

    protected Client() {}

    public Client(String username, String password, String name, String phoneNumber, String nif, Address address) {
        super(username, password, name, phoneNumber, nif);
        this.address = address;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
