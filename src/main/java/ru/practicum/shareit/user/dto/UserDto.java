package ru.practicum.shareit.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Value
@Builder
@Jacksonized
public class UserDto {
    @Schema(example = "1")
    Long id;
    @Schema(example = "User")
    String name;
    @NotBlank
    @Email
    @Schema(example = "user@test.com")
    String email;
}
