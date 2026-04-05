package pt.ul.fc.css.tascaeats.entities;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;

@Entity
public class Client extends AUser {
    
    @Embedded
    private Address address;

    public Client() {}

    public Client(String userName, String password, String name, String phoneNumber, String nif) {
        super(userName, password, name, phoneNumber, nif);
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
