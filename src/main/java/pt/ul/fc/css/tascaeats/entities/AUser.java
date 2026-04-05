package pt.ul.fc.css.tascaeats.entities;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="user_type") // nome da coluna com o tipo do usuário
public abstract class AUser {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String phoneNumber;

    @Column(unique = true, nullable = false)
    private String nif;

    @Embedded
    private Address address;

    protected AUser() {}

    public AUser(String userName, String password, String name, String phoneNumber, String nif, Address address) {
        this.username = userName;
        this.password = password;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.nif = nif;
        this.address = address;
    }

    /**
     * Factory method to create a user based on the role.
     * @param role The role of the user (e.g., "ADMIN", "EDITOR", "CUSTOMER")
     * @return AUser instance corresponding to the role
     */
    public static AUser create(String role) {
        return switch (role) {
            case "ADMIN" -> new Admin();
            case "EDITOR" -> new Client();
            case "CUSTOMER" -> new DeliveryPerson();
            default -> throw new IllegalArgumentException("Invalid role: " + role);
        };
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getNif() {
        return nif;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
