package ru.practicum.shareit.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@AllArgsConstructor
@Getter
public class ResponseUserDto implements Serializable {
    private final Long id;
    @Schema(example = "User")
    private final String name;
    @Schema(example = "user@test.com")
    private final String email;
}