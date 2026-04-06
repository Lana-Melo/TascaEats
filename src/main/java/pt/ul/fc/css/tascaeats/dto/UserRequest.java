package pt.ul.fc.css.tascaeats.dto;

public class UserRequest {
    private String username;
    private String password;
    private String name;
    private String phoneNumber;
    private String nif;
    private String role; // client, admin ou deliveryMan por agora fica string mas eventualmente podiamos ter enum pra isto
                        //vai depender tbm de como vamos representar a herança nas tabelas (single table, joined, table per class lalalala )

    public UserRequest() {}

    public String getUsername() { return username; }
    public void setUsername(String userName) { this.username = userName; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public String getNif() { return nif; }
    public void setNif(String nif) { this.nif = nif; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

}