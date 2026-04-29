package pt.ul.fc.css.tascaeats.dto.restaurant;

public record RestaurantResponseDto(
    Long id,
    String designation,
    String status,
    String nif,
    String adminName,
    String street,
    String city,
    String postalCode,
    String openHours,
    String closeHours,
    Long menuId) {}
