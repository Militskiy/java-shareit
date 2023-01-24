package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;
import ru.practicum.shareit.booking.dto.BookingShortResponse;

import java.io.Serializable;
import java.util.Set;

@Value
@Builder
@Jacksonized
public class ItemResponse implements Serializable {
    Long id;
    String name;
    String description;
    Boolean available;
    BookingShortResponse lastBooking;
    BookingShortResponse nextBooking;
    Set<CommentResponse> comments;
}