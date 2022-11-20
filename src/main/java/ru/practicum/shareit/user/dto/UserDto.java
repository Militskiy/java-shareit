package ru.practicum.shareit.user.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonIgnore
    Long id;
    @Schema(example = "User")
    String name;
    @NotBlank
    @Email
    @Schema(example = "user@test.com")
    String email;

    @JsonProperty
    public Long getId() {
        return id;
    }
}
