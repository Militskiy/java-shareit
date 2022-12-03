package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dto.ItemShortResponseDto;
import ru.practicum.shareit.user.dto.UserIdDto;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * A DTO for the {@link ru.practicum.shareit.booking.model.Booking} entity
 */
@AllArgsConstructor
@Getter
public class BookingResponseDto implements Serializable {
    private final Long id;
    private final LocalDateTime start;
    private final LocalDateTime end;
    private final ItemShortResponseDto item;
    private final UserIdDto booker;
    private final Status status;
}