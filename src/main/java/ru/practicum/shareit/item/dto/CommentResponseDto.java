package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * A DTO for the {@link ru.practicum.shareit.item.model.Comment} entity
 */
@AllArgsConstructor
@Getter
public class CommentResponseDto implements Serializable {
    private final Long id;
    private final String text;
    private final String authorName;
    private final LocalDateTime created;
}