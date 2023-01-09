package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Value
@Builder
@Jacksonized
public class BookingListDto {
    @JsonValue
    List<BookingResponseDto> bookings;
}
