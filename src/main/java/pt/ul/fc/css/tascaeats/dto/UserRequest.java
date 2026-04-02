package pt.ul.fc.css.tascaeats.dto;

public class UserRequest {
    private String userName;
    private String name;
    private String phoneNumber;
    private String NIF;
    private String role; // client, admin ou deliveryMan por agora fiac string mas eventualmente podiamos ter enum pra iso
                        //vai depender tbm de como vamos representar a herança nas tabelas (single table, joined, table per class lalalala )

    public UserRequest() {}

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public String getNIF() { return NIF; }
    public void setNIF(String NIF) { this.NIF = NIF; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

}
