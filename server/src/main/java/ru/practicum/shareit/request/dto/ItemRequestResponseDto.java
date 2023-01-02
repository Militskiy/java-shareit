package ru.practicum.shareit.request.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;
import ru.practicum.shareit.item.dto.ResponseItemDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * A DTO for the {@link ItemRequest} entity
 */
@Value
@Builder
@Jacksonized
public class ItemRequestResponseDto implements Serializable {
    Long id;
    String description;
    LocalDateTime created;
    Set<ResponseItemDto> items;
}