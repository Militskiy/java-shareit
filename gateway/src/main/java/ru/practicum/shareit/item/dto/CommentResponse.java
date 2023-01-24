package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.io.Serializable;
import java.time.LocalDateTime;

@Value
@Builder
@Jacksonized
public class CommentResponse implements Serializable {
    Long id;
    String text;
    String authorName;
    LocalDateTime created;
}