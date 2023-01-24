package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.io.Serializable;

@Value
@Builder
@Jacksonized
public class BookingShortResponse implements Serializable {
    Long id;
    Long bookerId;
}