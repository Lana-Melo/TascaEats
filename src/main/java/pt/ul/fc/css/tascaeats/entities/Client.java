package pt.ul.fc.css.tascaeats.entities;

import java.time.LocalDateTime;

import org.hibernate.annotations.Where;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;

@Entity
@Where(clause = "deleted_at IS NULL") // For soft deletes
public class Client extends AUser {
    
    @Embedded
    private Address address;
    
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt; // For soft deletes

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

    public void setDeletedAt(LocalDateTime deletedAt){
        this.deletedAt = deletedAt;
    }

    public LocalDateTime getDeletedAt(){
        return this.deletedAt;
    }
}
