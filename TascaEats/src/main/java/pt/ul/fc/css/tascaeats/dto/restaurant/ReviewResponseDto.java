package pt.ul.fc.css.tascaeats.dto.restaurant;

public record ReviewResponseDto(
    Long id, int rating, String comment, Long clientId, Long restaurantId) {}
