package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.io.Serializable;

@Value
@Builder
@Jacksonized
public class ItemShortResponseDto implements Serializable {
    Long id;
    String name;
}