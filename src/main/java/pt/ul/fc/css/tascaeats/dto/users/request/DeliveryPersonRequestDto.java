package pt.ul.fc.css.tascaeats.dto.users.request;

import jakarta.validation.constraints.NotBlank;

public class DeliveryPersonRequestDto {
  @NotBlank private String username;
  @NotBlank private String password;
  @NotBlank private String name;
  @NotBlank private String phoneNumber;
  @NotBlank private String nif;

  public DeliveryPersonRequestDto() {}

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public String getNif() {
    return nif;
  }

  public void setNif(String nif) {
    this.nif = nif;
  }
}
