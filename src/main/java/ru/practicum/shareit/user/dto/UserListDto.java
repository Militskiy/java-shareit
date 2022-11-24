package ru.practicum.shareit.user.dto;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Value
@Builder
@Jacksonized
public class UserListDto {
    @JsonValue
    List<UserDto> userDtoList;
}
