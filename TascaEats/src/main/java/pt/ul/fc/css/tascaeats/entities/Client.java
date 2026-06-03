package pt.ul.fc.css.tascaeats.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Client extends AUser {

  @ElementCollection
  @CollectionTable(name = "client_addresses", joinColumns = @JoinColumn(name = "client_id"))
  private List<Address> addresses;

  @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
  private List<Review> reviews;

  protected Client() {}

  public Client(
      String username,
      String password,
      String name,
      String phoneNumber,
      String nif,
      List<Address> addresses) {
    super(username, password, name, phoneNumber, nif);
    this.addresses = addresses;
    this.reviews = new ArrayList<>();
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

  public List<Review> getReviews() {
    return this.reviews;
  }

  public void addReview(Review review) {
    this.reviews.add(review);
  }

  public void setReviews(List<Review> reviews) {
    this.reviews = reviews;
  }
}
