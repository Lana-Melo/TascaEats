package pt.ul.fc.css.tascaeats.entities;

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
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import pt.ul.fc.css.tascaeats.entities.enums.OrderStatus;

@Entity
@Table(name = "orders")
public class Order {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, columnDefinition = "TIMESTAMP")
  private LocalDateTime timestamp;

  private double totalValue;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private OrderStatus status;

  @Embedded private Address address;

  @ManyToOne
  @JoinColumn(name = "client_id", nullable = false)
  private Client client;

  @ManyToMany
  @JoinTable(
      name = "order_productitems",
      joinColumns = @JoinColumn(name = "order_id"),
      inverseJoinColumns = @JoinColumn(name = "productitem_id"))
  private List<ProductItem> productItems;

  @ManyToOne
  @JoinColumn(name = "delivery_person_id")
  private DeliveryPerson deliveryPerson;

  private Long deliveryId;

  // colocando aqui para nao quebrar ainda que nao temos a comunicacao com o ms
  private Delivery delivery;

  @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "payment_id")
  private APayment payment;

  protected Order() {}

  public Order(Client client, List<ProductItem> productItems, Address address) {
    this.timestamp = LocalDateTime.now();
    this.status = OrderStatus.CREATED;
    this.client = client;
    this.productItems = productItems;
    this.address = address;
    this.totalValue = 0.0;

    for (ProductItem productItem : productItems) {
      this.totalValue = this.totalValue + productItem.getProduct().getPrice();
    }
  }

  public Long getId() {
    return id;
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

  public List<ProductItem> getProductItems() {
    return productItems;
  }

  public void setProductItems(List<ProductItem> productItems) {
    this.productItems = productItems;
  }

  public void addProductItem(ProductItem productItems) {
    this.productItems.add(productItems);
  }

  public Long getDeliveryId() {
    return deliveryId;
  }

  public void setDeliveryId(Long deliveryId) {
    this.deliveryId = deliveryId;
  }

  public APayment getPayment() {
    return payment;
  }

  public void setPayment(APayment payment) {
    this.payment = payment;
  }

  public void setDeliveryPerson(DeliveryPerson deliveryPerson) {
    this.deliveryPerson = deliveryPerson;
  }

  public DeliveryPerson getDeliveryPerson() {
    return deliveryPerson;
  }

  public Address getAddress() {
    return address;
  }

  public Delivery getDelivery() {
    return delivery;
  }

  public void setDelivery(Delivery delivery) {
    this.delivery = delivery;
  }
}
