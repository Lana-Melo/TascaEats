package pt.ul.fc.css.tascaeats.entities;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.Where;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;

@Entity
@Where(clause = "deleted_at IS NULL") // For soft deletes
public class Client extends AUser {
    
    @ManyToMany
    @JoinTable(
        name = "client_address", 
        joinColumns = @JoinColumn(name = "client_id"), 
        inverseJoinColumns = @JoinColumn(name = "address_id"))
    private List<Address> addresses;
    
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt; // For soft deletes

    protected Client() {}

    public Client(String username, String password, String name, String phoneNumber, String nif, List<Address> addresses) {
        super(username, password, name, phoneNumber, nif);
        this.addresses = addresses;
    }

    public List<Address> getAddresses() {
        return this.addresses;
    }

    public void addAddress(Address address) {
        this.addresses.add(address);
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    public void setDeletedAt(LocalDateTime deletedAt){
        this.deletedAt = deletedAt;
    }

    public LocalDateTime getDeletedAt(){
        return this.deletedAt;
    }
}
