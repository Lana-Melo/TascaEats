package pt.ul.fc.css.tascaeats.dto.restaurant;

public record ReviewResponse (
    Long id, 
    int rating,
    String comment,
    Long clientId,
    Long restaurantId
) {}
