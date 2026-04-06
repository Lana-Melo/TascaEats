package pt.ul.fc.css.tascaeats.dto;

//record porque a resposta é imutavel, so lemos, nunca modificamos
public record RestaurantResponse(
    Long id, //aquele gerado pelo spring 
    String designation,
    String status,
    String nif,
    String adminName,
    String street,
    String city,
    String postalCode
    // menu e orders vao estar aqui?
) {}
