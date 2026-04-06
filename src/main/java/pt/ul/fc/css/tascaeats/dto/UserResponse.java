package pt.ul.fc.css.tascaeats.dto;

//record porque a resposta é imutavel, so lemos, nunca modificamos
public record UserResponse(
    Long id, //aquele gerado pelo spring 
    String username,
    String name,
    String phoneNumber,
    String nif,
    String role
) {}

