package ru.practicum.shareit.item.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.io.Serializable;

/**
 * A DTO for the {@link ru.practicum.shareit.item.model.Item} entity
 */
@Value
@Builder
@Jacksonized
public class ItemDto implements Serializable {
    @Schema(example = "Item")
    String name;
    @Schema(example = "Test item")
    String description;
    @Schema(example = "true")
    Boolean available;
    Long requestId;
}
