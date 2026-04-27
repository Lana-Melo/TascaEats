package pt.ul.fc.css.tascaeats.dto.restaurant;

public record RestaurantResponse(
    Long id,
    String designation,
    String status,
    String nif,
    String adminName,
    String street,
    String city,
    String postalCode,
    Long menuId
) {}
