package pt.ul.fc.css.tascaeats.dto.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ProductRequest {
    @NotNull
    private double price;
    @NotBlank
    private String description;
    @NotBlank
    private String designation;
    @NotBlank
    private String availability; 

    public ProductRequest() {}

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getDesignation() { return designation; }
    public void setDesignation(String designation) { this.designation = designation; }
    public String getAvailability() { return availability; }
    public void setAvailability(String availability) { this.availability = availability; }
}