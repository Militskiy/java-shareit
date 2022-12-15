package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.practicum.shareit.item.dto.ResponseItemDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * A DTO for the {@link ItemRequest} entity
 */
@AllArgsConstructor
@Getter
public class ItemRequestResponseDto implements Serializable {
    private final Long id;
    private final String description;
    private final LocalDateTime created;
    private final Set<ResponseItemDto> items;
}