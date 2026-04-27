package pt.ul.fc.css.tascaeats.dto.menu;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class MenuRequest {
    @NotBlank
    private String name;
    @NotBlank
    private String cuisine;
    @NotBlank
    private String description;
    @NotNull
    private List<Long> productIds;

    public MenuRequest() {}

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCuisine() { return cuisine; }    
    public void setCuisine(String cuisine) { this.cuisine = cuisine; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public List<Long> getProductIds() { return productIds; }
    public void setProductIds(List<Long> productIds) { this.productIds = productIds; }
}
