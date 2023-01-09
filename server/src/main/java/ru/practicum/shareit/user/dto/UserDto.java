package ru.practicum.shareit.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.io.Serializable;

/**
 * A DTO for the {@link ru.practicum.shareit.user.model.User} entity
 */
@Value
@Builder
@Jacksonized
public class UserDto implements Serializable {
    @Schema(example = "User")
    String name;
    @Schema(example = "user@test.com")
    String email;
}
