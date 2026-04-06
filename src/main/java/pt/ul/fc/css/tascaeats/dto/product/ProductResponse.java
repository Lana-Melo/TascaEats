package pt.ul.fc.css.tascaeats.dto.product;

public record ProductResponse (
    Long id,
    double price,
    String description,
    String designation,
    String availability
) {}
