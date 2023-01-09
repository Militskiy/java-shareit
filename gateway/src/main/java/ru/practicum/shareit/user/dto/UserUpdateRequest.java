package ru.practicum.shareit.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

@Valid
@Value
@Builder
@Jacksonized
public class UserUpdateRequest {
    @Pattern(regexp = "^[^ ].*[^ .]$", message = "name is not valid")
    @Schema(example = "User")
    String name;
    @Email
    @Schema(example = "user@test.com")
    String email;
}
