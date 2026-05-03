package pt.ul.fc.css.tascaeats.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import org.hibernate.annotations.Where;
import pt.ul.fc.css.tascaeats.entities.enums.ProductStatus;

@Entity
@Where(clause = "deleted_at IS NULL") // For soft deletes
public class ProductItem {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private ProductStatus availability;

  @ManyToOne
  @JoinColumn(name = "restaurant_id", nullable = false)
  private Restaurant restaurant;

  @ManyToOne
  @JoinColumn(name = "product_id", nullable = false)
  private Product product;

  @Column(name = "deleted_at")
  private LocalDateTime deletedAt; // For soft deletes

  protected ProductItem() {}

  public ProductItem(Product product, Restaurant restaurant) {
    this.product = product;
    this.availability = ProductStatus.AVAILABLE;
    this.restaurant = restaurant;
  }

  public Long getId() {
    return id;
  }

  public Product getProduct() {
    return product;
  }

  public void setProduct(Product product) {
    this.product = product;
  }

  public Restaurant getRestaurant() {
    return restaurant;
  }

  public void setRestaurant(Restaurant restaurant) {
    this.restaurant = restaurant;
  }

  public ProductStatus getStatus() {
    return availability;
  }

  public void setStatus(ProductStatus availability) {
    this.availability = availability;
  }

  public Object getDeletedAt() {
    return this.deletedAt;
  }

  public void setDeletedAt(LocalDateTime deletedAt) {
    this.deletedAt = deletedAt;
  }
}
