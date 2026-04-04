package pt.ul.fc.css.tascaeats.dto;

public class RestaurantRequest {
    private String userName; 
    private String designation;
    private String status; 
    private String nif;

    public RestaurantRequest() {}

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    public String getDesignation() { return designation; }
    public void setDesignation(String designation) { this.designation = designation; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getNif() { return nif; }
    public void setNif(String nif) { this.nif = nif; }
}

