package pt.ul.fc.css.tascaeats.dto.users.request;

import jakarta.validation.constraints.NotBlank;

public class LoginRequestDto {
  @NotBlank private String username;
  private String password;

  public LoginRequestDto() {}

  public String getUsername() {
    return username;
  }

  public void setUsername(String userName) {
    this.username = userName;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}
