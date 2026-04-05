package pt.ul.fc.css.tascaeats.entities;

import jakarta.persistence.*;

@Entity
public class Client extends User {
    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "adress_id", nullable = false)
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
