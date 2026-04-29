package pt.ul.fc.css.tascaeats.dto.users.response;

public record ClientResponseDto(
    Long id,
    String username,
    String name,
    String phoneNumber,
    String nif,
    String street,
    String city,
    String postalCode) {}
