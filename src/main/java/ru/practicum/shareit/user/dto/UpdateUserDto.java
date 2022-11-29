package ru.practicum.shareit.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

@Valid
@Value
@Builder
@Jacksonized
public class UpdateUserDto implements Serializable {

    @Schema(example = "User")
    @Pattern(regexp = "^[^ ].*[^ .]$", message = "Name is not valid")
    String name;
    @Email
    @Schema(example = "user@test.com")
    String email;
}