package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;
import ru.practicum.shareit.item.dto.ItemShortResponse;
import ru.practicum.shareit.user.dto.UserIdResponse;

import java.io.Serializable;
import java.time.LocalDateTime;

@Value
@Builder
@Jacksonized
public class BookingResponse implements Serializable {
    Long id;
    LocalDateTime start;
    LocalDateTime end;
    ItemShortResponse item;
    UserIdResponse booker;
    Status status;
}