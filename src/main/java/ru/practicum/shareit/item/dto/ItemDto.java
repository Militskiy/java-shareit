package ru.practicum.shareit.item.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Value
@Builder
@Jacksonized
public class ItemDto {
    Long id;
    @NotBlank
    @Schema(example = "Item")
    String name;
    @NotBlank
    @Schema(example = "Test item")
    String description;
    @NotNull
    @Schema(example = "true")
    Boolean available;
}
