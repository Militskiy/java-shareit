package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.io.Serializable;

/**
 * A DTO for the {@link ru.practicum.shareit.item.model.Item} entity
 */
@Value
@Builder(toBuilder = true)
@Jacksonized
public class ResponseItemDto implements Serializable {
    Long id;
    String name;
    String description;
    Boolean available;
    Long requestId;
}