package pt.ul.fc.css.tascaeats.common.dto;

public record DeliveryPersonResponseDto(
    Long id, String username, String name, String phoneNumber, String nif, String status) {}
