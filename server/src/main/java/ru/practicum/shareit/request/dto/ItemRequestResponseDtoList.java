package ru.practicum.shareit.request.dto;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.io.Serializable;
import java.util.List;

@Value
@Builder
@Jacksonized
public class ItemRequestResponseDtoList implements Serializable {
    @JsonValue
    List<ItemRequestResponseDto> itemRequests;
}
