package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import javax.validation.Valid;
import java.io.Serializable;

/**
 * A DTO for the {@link ru.practicum.shareit.item.model.Comment} entity
 */
@Value
@Builder
@Jacksonized
@Valid
public class CommentCreateDto implements Serializable {
    String text;
}