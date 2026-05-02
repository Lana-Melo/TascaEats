package pt.ul.fc.css.tascaeats.dto.users.response;

import java.util.List;

public record ClientResponseDto(
    Long id,
    String username,
    String name,
    String phoneNumber,
    String nif,
    List<AddressDto> addresses) {}
