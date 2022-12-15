package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * A DTO for the {@link ru.practicum.shareit.request.model.ItemRequest} entity
 */
@AllArgsConstructor
@Getter
public class ItemRequestCreateResponseDto implements Serializable {
    private final Long id;
    private final String description;
    private final LocalDateTime created;
}