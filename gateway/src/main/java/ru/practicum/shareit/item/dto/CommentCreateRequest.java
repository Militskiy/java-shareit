package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@Value
@Builder
@Jacksonized
@Valid
public class CommentCreateRequest {
    @NotBlank
    String text;
}
