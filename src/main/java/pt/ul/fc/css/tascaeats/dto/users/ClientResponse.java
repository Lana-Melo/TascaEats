package pt.ul.fc.css.tascaeats.dto.users;

//record porque a resposta é imutavel, so lemos, nunca modificamos
public record ClientResponse(
    Long id, //aquele gerado pelo spring 
    String userName,
    String name,
    String phoneNumber,
    String nif,
    String city,
    String street,
    String postalCode
) {}

