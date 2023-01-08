package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * A DTO for the {@link ru.practicum.shareit.booking.model.Booking} entity
 */
@Value
@Builder
@Jacksonized
public class BookingCreateDto implements Serializable {
    LocalDateTime start;
    LocalDateTime end;
    Long itemId;
}