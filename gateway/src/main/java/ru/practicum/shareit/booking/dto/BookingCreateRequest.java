package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;
import ru.practicum.shareit.booking.annotations.EndAfterStart;

import javax.validation.Valid;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@Valid
@Value
@Builder
@Jacksonized
@EndAfterStart
public class BookingCreateRequest implements Serializable {
    @Future(message = "start date must be in the future")
    @NotNull(message = "start date cannot be null")
    LocalDateTime start;
    @Future(message = "end date must be in the future")
    @NotNull(message = "end date cannot be null")
    LocalDateTime end;
    @NotNull(message = "item id cannot be null")
    Long itemId;
}
