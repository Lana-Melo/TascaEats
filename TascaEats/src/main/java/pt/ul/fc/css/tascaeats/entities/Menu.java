package pt.ul.fc.css.tascaeats.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.Where;

@Entity
@Where(clause = "deleted_at IS NULL") // For soft deletes
public class Menu {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String cuisine;

  @Column(nullable = false)
  private String description;

  @ManyToMany
  @JoinTable(
      name = "menu_products",
      joinColumns = @JoinColumn(name = "menu_id"),
      inverseJoinColumns = @JoinColumn(name = "product_id"))
  private List<Product> products;

  @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL)
  private List<Restaurant> restaurants;

  @Column(name = "deleted_at")
  private LocalDateTime deletedAt; // For soft deletes

  protected Menu() {}

  public Menu(String name, String cuisine, String description) {
    this.name = name;
    this.cuisine = cuisine;
    this.description = description;
    this.products = new ArrayList<>();
    this.restaurants = new ArrayList<>();
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getCuisine() {
    return cuisine;
  }

  public void setCuisine(String cuisine) {
    this.cuisine = cuisine;
  }

  public List<Product> getProducts() {
    return products;
  }

  public void setProducts(List<Product> products) {
    this.products = products;
  }

  public void addProduct(Product product) {
    this.products.add(product);
  }

  public List<Restaurant> getRestaurants() {
    return restaurants;
  }

  public void setRestaurants(List<Restaurant> restaurants) {
    this.restaurants = restaurants;
  }

  public void addRestaurant(Restaurant restaurant) {
    this.restaurants.add(restaurant);
  }

  public void setDeletedAt(LocalDateTime deletedAt) {
    this.deletedAt = deletedAt;
  }

  public Object getDeletedAt() {
    return this.deletedAt;
  }
}
