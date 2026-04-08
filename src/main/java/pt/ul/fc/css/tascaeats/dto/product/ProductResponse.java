package pt.ul.fc.css.tascaeats.dto.product;

public record ProductResponse (
    Long id,
    Double price,
    String description,
    String designation,
    String availability
) {}
