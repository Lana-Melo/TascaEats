package pt.ul.fc.css.tascaeats.dto.menu;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public class MenuRequestDto {
  @NotBlank private String name;
  @NotBlank private String cuisine;
  @NotBlank private String description;
  @NotNull private List<Long> productIds;

  public MenuRequestDto() {}

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getCuisine() {
    return cuisine;
  }

  public void setCuisine(String cuisine) {
    this.cuisine = cuisine;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public List<Long> getProductIds() {
    return productIds;
  }

  public void setProductIds(List<Long> productIds) {
    this.productIds = productIds;
  }
}
