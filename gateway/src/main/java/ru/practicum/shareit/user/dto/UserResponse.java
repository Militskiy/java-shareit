package ru.practicum.shareit.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.io.Serializable;

@Value
@Builder
@Jacksonized
public class UserResponse implements Serializable {
    Long id;
    @Schema(example = "User")
    String name;
    @Schema(example = "user@test.com")
    String email;
}