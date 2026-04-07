package pt.ul.fc.css.tascaeats.dto.users.response;

public record ClientResponse(
    Long id,
    String userName,
    String name,
    String phoneNumber,
    String nif,
    String city,
    String street,
    String postalCode
) {}

