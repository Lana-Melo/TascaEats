package pt.ul.fc.css.tascaeats.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;

@Entity
public class Review {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private int rating;

  @Column(nullable = false)
  private String comment;

  @Column(nullable = false)
  private LocalDateTime createdAt;

  @ManyToOne
  @JoinColumn(name = "client_id", nullable = false)
  private Client client;

  @ManyToOne
  @JoinColumn(name = "restaurant_id", nullable = false)
  private Restaurant restaurant;

  protected Review() {}

  public Review(int rating, String comment, Client client, Restaurant restaurant) {
    this.rating = rating;
    this.comment = comment;
    this.client = client;
    this.restaurant = restaurant;
    this.createdAt = LocalDateTime.now();
  }

  public Long getId() {
    return id;
  }

  public int getRating() {
    return rating;
  }

  public void setRating(int rating) {
    this.rating = rating;
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public Client getClient() {
    return client;
  }

  public Restaurant getRestaurant() {
    return restaurant;
  }
}
