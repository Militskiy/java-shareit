package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * A DTO for the {@link ru.practicum.shareit.item.model.Comment} entity
 */
@Value
@Builder
@Jacksonized
public class CommentResponseDto implements Serializable {
    Long id;
    String text;
    String authorName;
    LocalDateTime created;
}