package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

/**
 * A DTO for the {@link ru.practicum.shareit.booking.model.Booking} entity
 */
@AllArgsConstructor
@Getter
public class BookingShortDto implements Serializable {
    private final Long id;
    private final Long bookerId;
}