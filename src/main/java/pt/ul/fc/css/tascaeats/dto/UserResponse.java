package pt.ul.fc.css.tascaeats.dto;

//record porque a resposta é imutavel, so lemos, nunca modificamos
public record UserResponse(
    Long id, //aquele gerado pelo spring 
    String userName,
    String name,
    String phoneNumber,
    String NIF,
    String role
) {}

