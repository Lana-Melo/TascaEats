package pt.ul.fc.css.tascaeats.dto.users;

import jakarta.validation.constraints.NotNull;

public class ClientRequest {
    @NotNull
    private String username;
    @NotNull
    private String password;
    @NotNull
    private String name;
    @NotNull
    private String phoneNumber;
    @NotNull
    private String nif;
    @NotNull
    private String city;
    @NotNull
    private String street;
    @NotNull
    private String postalCode;

    public ClientRequest() {}

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public String getNif() { return nif; }
    public void setNif(String nif) { this.nif = nif; }
    public String getCity() { return city; }    
    public void setCity(String city) { this.city = city; }
    public String getStreet() { return street; }    
    public void setStreet(String street) { this.street = street; }
    public String getPostalCode() { return postalCode; }
    public void setPostalCode(String postalCode) { this.postalCode = postalCode; }


}
