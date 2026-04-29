package pt.ul.fc.css.tascaeats.dto.users.response;

public record DeliveryPersonResponseDto(
    Long id, String username, String name, String phoneNumber, String nif, String status) {}
