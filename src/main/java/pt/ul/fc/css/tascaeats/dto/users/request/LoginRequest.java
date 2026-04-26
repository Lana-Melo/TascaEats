package pt.ul.fc.css.tascaeats.dto.users.request;

import jakarta.validation.constraints.NotBlank;

public class LoginRequest {
    @NotBlank
    private String username;
    private String password;

    public LoginRequest() {}

    public String getUsername() { return username; }
    public void setUsername(String userName) { this.username = userName; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}

