package pt.ul.fc.css.tascaeats.dto.user;

public record DeliveryPersonResponse (
    Long id, //aquele gerado pelo spring 
    String userName,
    String name,
    String phoneNumber,
    String nif,
    String status
) {}
