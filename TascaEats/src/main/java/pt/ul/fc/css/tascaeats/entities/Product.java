package pt.ul.fc.css.tascaeats.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import org.hibernate.annotations.Where;

@Entity
@Where(clause = "deleted_at IS NULL") // For soft deletes
public class Product {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private double price;

  @Column(nullable = false)
  private String description;

  @Column(nullable = false)
  private String designation;

  @ManyToOne
  @JoinColumn(name = "menu_id", nullable = false)
  private Menu menu;

  @Column(name = "deleted_at")
  private LocalDateTime deletedAt; // For soft deletes

  protected Product() {}

  public Product(double price, String description, String designation, Menu menu) {
    this.price = price;
    this.description = description;
    this.designation = designation;
    this.menu = menu;
  }

  public Long getId() {
    return id;
  }

  public double getPrice() {
    return price;
  }

  public void setPrice(double price) {
    this.price = price;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getDesignation() {
    return designation;
  }

  public void setDesignation(String designation) {
    this.designation = designation;
  }

  public Menu getMenu() {
    return menu;
  }

  public void setMenu(Menu menu) {
    this.menu = menu;
  }

  public void setDeletedAt(LocalDateTime deletedAt) {
    this.deletedAt = deletedAt;
  }

  public Object getDeletedAt() {
    return this.deletedAt;
  }
}
