package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

/**
 * A DTO for the {@link ru.practicum.shareit.item.model.Item} entity
 */
@AllArgsConstructor
@Getter
public class ResponseItemDto implements Serializable {
    private final Long id;
    private final String name;
    private final String description;
    private final Boolean available;
}