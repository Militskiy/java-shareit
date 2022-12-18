package ru.practicum.shareit.request.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * A DTO for the {@link ru.practicum.shareit.request.model.ItemRequest} entity
 */
@Value
@Builder
@Jacksonized
public class ItemRequestCreateResponseDto implements Serializable {
    Long id;
    String description;
    LocalDateTime created;
}