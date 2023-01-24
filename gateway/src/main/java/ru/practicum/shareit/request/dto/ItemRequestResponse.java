package ru.practicum.shareit.request.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;
import ru.practicum.shareit.item.dto.ItemWithRequestResponse;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

@Value
@Builder
@Jacksonized
public class ItemRequestResponse implements Serializable {
    Long id;
    String description;
    LocalDateTime created;
    Set<ItemWithRequestResponse> items;
}