package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.io.Serializable;

@Value
@Builder
@Jacksonized
public class UserIdResponse implements Serializable {
    Long id;
}