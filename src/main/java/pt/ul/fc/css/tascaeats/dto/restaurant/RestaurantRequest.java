package pt.ul.fc.css.tascaeats.dto.restaurant;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class RestaurantRequest {
    @NotBlank
    private String designation;
    @NotBlank
    private String nif;
    @NotBlank
    private String city;
    @NotBlank
    private String postalCode;
    @NotBlank
    private String street;
    @NotNull
    private Long adminId;

    public RestaurantRequest() {}

    public String getDesignation() { return designation; }
    public void setDesignation(String designation) { this.designation = designation; }
    public String getNif() { return nif; }
    public void setNif(String nif) { this.nif = nif; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public String getPostalCode() { return postalCode; } 
    public void setPostalCode(String postalCode) { this.postalCode = postalCode; }
    public String getStreet() { return street; }
    public void setStreet(String street) { this.street = street; }
    public Long getAdminId() { return adminId; }
    public void setAdminId(Long adminId) { this.adminId = adminId; }
}

