package pt.ul.fc.css.tascaeats.dto.order;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public class OrderRequestDto {
  @NotNull private Long clientId;
  @NotNull private List<Long> productIds;
  @NotBlank private String city;
  @NotBlank private String street;
  @NotBlank private String postalCode;

  public OrderRequestDto() {}

  public Long getClientId() {
    return clientId;
  }

  public void setClientId(Long clientId) {
    this.clientId = clientId;
  }

  public List<Long> getProductIds() {
    return productIds;
  }

  public void setProductIds(List<Long> productIds) {
    this.productIds = productIds;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getStreet() {
    return street;
  }

  public void setStreet(String street) {
    this.street = street;
  }

  public String getPostalCode() {
    return postalCode;
  }

  public void setPostalCode(String postalCode) {
    this.postalCode = postalCode;
  }
}
