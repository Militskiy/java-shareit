package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.booking.dto.BookingShortDto;

import java.io.Serializable;
import java.util.Set;

/**
 * A DTO for the {@link ru.practicum.shareit.item.model.Item} entity
 */
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ItemWithBookingsDto implements Serializable {
    private final Long id;
    private final String name;
    private final String description;
    private final Boolean available;
    private BookingShortDto lastBooking;
    private BookingShortDto nextBooking;
    private Set<CommentResponseDto> comments;
}