package pt.ul.fc.css.tascaeats.dto.menu;

import java.util.List;

public record MenuResponseDto(
    Long id, String name, String cuisine, String description, List<Long> productIds) {}
