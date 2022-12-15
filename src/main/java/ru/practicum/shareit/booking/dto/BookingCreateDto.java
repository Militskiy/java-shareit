package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.practicum.shareit.booking.annotations.EndAfterStart;

import javax.validation.Valid;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * A DTO for the {@link ru.practicum.shareit.booking.model.Booking} entity
 */
@AllArgsConstructor(onConstructor = @__(@JsonCreator))
@Getter
@Valid
@EndAfterStart
public class BookingCreateDto implements Serializable {
    @Future(message = "start date must be in the future")
    @NotNull(message = "start date cannot be null")
    private final LocalDateTime start;
    @Future(message = "end date must be in the future")
    @NotNull(message = "end date cannot be null")
    private final LocalDateTime end;
    @NotNull(message = "item id cannot be null")
    private final Long itemId;
}