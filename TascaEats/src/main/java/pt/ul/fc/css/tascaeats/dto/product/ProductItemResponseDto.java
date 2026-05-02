package pt.ul.fc.css.tascaeats.dto.product;

public record ProductItemResponseDto(
    Long id,
    Long productId,
    Long restaurantId,
    String designation,
    String description,
    String availability) {}
