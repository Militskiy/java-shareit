package ru.practicum.shareit.request.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * A DTO for the {@link ru.practicum.shareit.request.model.ItemRequest} entity
 */
@Value
@Builder
@Jacksonized
@Valid
public class ItemRequestCreateDto implements Serializable {
    @NotBlank
    String description;
}