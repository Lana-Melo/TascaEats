package pt.ul.fc.css.tascaeats.dto.restaurant;

public class RestaurantRequest {
    private String designation;
    private String status; 
    private String nif;
    private String city;
    private String postalCode;
    private String street;
    private Long adminId; // importante para criar o admin (UserCase E)

    public RestaurantRequest() {}

    public String getDesignation() { return designation; }
    public void setDesignation(String designation) { this.designation = designation; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
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

