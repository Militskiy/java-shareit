package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.io.Serializable;

@Value
@Builder(toBuilder = true)
@Jacksonized
public class ItemWithRequestResponse implements Serializable {
    Long id;
    String name;
    String description;
    Boolean available;
    Long requestId;
}