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
import jakarta.persistence.OneToMany;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import pt.ul.fc.css.tascaeats.entities.enums.RestaurantStatus;

@Entity
public class Restaurant {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String designation;

  @Column(nullable = false)
  private String nif;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private RestaurantStatus status;

  @Column(nullable = false)
  private LocalTime openHours;

  @Column(nullable = false)
  private LocalTime closeHours;

  @Embedded
  private Address address;

  @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<ProductItem> productItems;

  @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL)
  private List<Review> reviews;

  @ManyToMany
  @JoinTable(
      name = "restaurant_orders",
      joinColumns = @JoinColumn(name = "restaurant_id"),
      inverseJoinColumns = @JoinColumn(name = "order_id"))
  private List<Order> orders;

  @ManyToOne
  @JoinColumn(name = "admin_id")
  private Admin admin;

  @ManyToOne
  @JoinColumn(name = "menu_id")
  private Menu menu;

  protected Restaurant() {}

  public Restaurant(String nif, String designation, Address address, Admin admin, LocalTime openHours, LocalTime closeHours) {
    this.nif = nif;
    this.designation = designation;
    this.status = RestaurantStatus.OPEN;
    this.address = address;
    this.productItems = new ArrayList<ProductItem>();
    this.orders = new ArrayList<Order>();
    this.reviews = new ArrayList<Review>();
    this.admin = admin;
    this.openHours = openHours;
    this.closeHours = closeHours;
  }

  public Long getId() {
    return id;
  }

  public String getDesignation() {
    return designation;
  }

  public void setDesignation(String designation) {
    this.designation = designation;
  }

  public String getNif() {
    return nif;
  }

  public void setNif(String nif) {
    this.nif = nif;
  }

  public RestaurantStatus getStatus() {
    return status;
  }

  public void setStatus(RestaurantStatus status) {
    this.status = status;
  }

  public Address getAddress() {
    return address;
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

  public List<Review> getReviews() {
    return this.reviews;
  }

  public void addReview(Review review) {
    this.reviews.add(review);
  }

  public void setReviews(List<Review> reviews) {
    this.reviews = reviews;
  }

  public Admin getAdmin() {
    return admin;
  }

  public void setAdmin(Admin admin) {
    this.admin = admin;
  }

  public List<Order> getOrders() {
    return orders;
  }

  public void addOrder(Order order) {
    this.orders.add(order);
  }

  public void setOrders(List<Order> order) {
    this.orders = order;
  }

  public Menu getMenu() {
    return menu;
  }

  public void setMenu(Menu menu) {
    this.menu = menu;
  }

  public LocalTime getOpenHours() {
    return openHours;
  }

  public void setOpenHours(LocalTime openHours) {
    this.openHours = openHours;
  }

  public LocalTime getCloseHours() {
      return closeHours;
  }

  public void setCloseHours(LocalTime closeHours) {
    this.closeHours = closeHours;
  }
}
