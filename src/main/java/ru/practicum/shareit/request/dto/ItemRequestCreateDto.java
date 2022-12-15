package ru.practicum.shareit.request.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * A DTO for the {@link ru.practicum.shareit.request.model.ItemRequest} entity
 */
@AllArgsConstructor(onConstructor = @__(@JsonCreator))
@Getter
@Valid
public class ItemRequestCreateDto implements Serializable {
    @NotBlank
    private final String description;
}