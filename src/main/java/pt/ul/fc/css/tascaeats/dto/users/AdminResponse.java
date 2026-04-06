package pt.ul.fc.css.tascaeats.dto.users;

public record AdminResponse (
    Long id, //aquele gerado pelo spring 
    String userName,
    String name,
    String phoneNumber,
    String nif
) {}
