package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;
import ru.practicum.shareit.booking.model.Status;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;


@Value
@Builder
@Jacksonized
public class BookingDto implements Serializable {
    Long id;
    Date start;
    Date end;
    Long itemId;
    @NotBlank
    String itemName;
    Long bookerId;
    Status status;
}