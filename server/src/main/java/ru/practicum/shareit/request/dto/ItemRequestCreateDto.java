package ru.practicum.shareit.request.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.io.Serializable;

/**
 * A DTO for the {@link ru.practicum.shareit.request.model.ItemRequest} entity
 */
@Value
@Builder
@Jacksonized
public class ItemRequestCreateDto implements Serializable {
    String description;
}