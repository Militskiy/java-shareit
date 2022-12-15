package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * A DTO for the {@link Item} entity
 */
@AllArgsConstructor
@Getter
public class ItemDtoRequest implements Serializable {
    private final Long id;
    private final String name;
    @NotNull
    private final String description;
    @NotNull
    private final Boolean available;
    private final Long requestId;
}