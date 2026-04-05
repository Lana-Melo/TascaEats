package pt.ul.fc.css.tascaeats.entities;

import pt.ul.fc.css.tascaeats.entities.enums.RestaurantStatus;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.*;


@Entity
public class Restaurant {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String designation;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RestaurantStatus status;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "adress_id", nullable = false)
    private Address address;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Product> products;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    private Admin admin;

    public Restaurant() {}

    public Restaurant(String designation, Address address, Admin admin) {
        this.designation = designation;
        this.status = RestaurantStatus.OPEN;
        this.address = address;
        this.products = new ArrayList<Product>();
        this.admin = admin;
    }

    public Long getId() {
        return id;
    }

    public String getDesignation() {
        return designation;
    }

    public RestaurantStatus getRestaurantStatus() {
        return status;
    }

    public void setRestaurantStatus(RestaurantStatus status) {
        this.status = status;
    }

    public Address getAddress() {
        return address;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }
}
