package pt.ul.fc.css.tascaeats.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import pt.ul.fc.css.tascaeats.entities.enums.OrderStatus;

@Entity
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    private double totalValue;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @ManyToMany
    @JoinTable(
        name = "order_products", 
        joinColumns = @JoinColumn(name = "order_id"), 
        inverseJoinColumns = @JoinColumn(name = "product_id"))
    private List<Product> products;

    @ManyToOne
    @JoinColumn(name = "delivery_person_id")
    private DeliveryPerson deliveryPerson;

    @Embedded
    private Delivery delivery;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "payment_id")
    private Payment payment;

    protected Order() {}

    public Order(String code, Client client, Restaurant restaurant, List<Product> products) {
        this.code = code;
        this.timestamp = LocalDateTime.now();
        this.status = OrderStatus.PREPARING;
        this.client = client;
        this.restaurant = restaurant;
        this.products = new ArrayList<Product>();
    }

    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public double getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(double totalValue) {
        this.totalValue = totalValue;
    }

    public OrderStatus getOrderStatus() {
        return status;
    }

    public void setOrderStatus(OrderStatus status) {
        this.status = status;
    }

    public Client getClient() {
        return client;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void addProduct(Product product) {
        this.products.add(product);
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public Delivery getDelivery() {
        return delivery;
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public void setDeliveryPerson(DeliveryPerson deliveryPerson) {
        this.deliveryPerson = deliveryPerson;
    }

    public DeliveryPerson getDeliveryPerson() {
        return deliveryPerson;
    }
}
