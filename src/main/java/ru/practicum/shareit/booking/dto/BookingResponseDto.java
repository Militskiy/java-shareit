package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dto.ItemShortResponseDto;
import ru.practicum.shareit.user.dto.UserIdDto;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * A DTO for the {@link ru.practicum.shareit.booking.model.Booking} entity
 */
@Value
@Builder
@Jacksonized
public class BookingResponseDto implements Serializable {
    Long id;
    LocalDateTime start;
    LocalDateTime end;
    ItemShortResponseDto item;
    UserIdDto booker;
    Status status;
}