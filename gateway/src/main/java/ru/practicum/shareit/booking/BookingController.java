package ru.practicum.shareit.booking;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.booking.client.BookingClient;
import ru.practicum.shareit.booking.dto.BookingCreateRequest;
import ru.practicum.shareit.booking.dto.State;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import static ru.practicum.shareit.BaseClient.HEADER_USER_ID;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Booking services")
@Validated
public class BookingController {
    private final BookingClient client;

    @GetMapping("/{id}")
    @Operation(summary = "Get booking information")
    public ResponseEntity<Object> findBooking(
            @PathVariable @Positive Long id,
            @RequestHeader(HEADER_USER_ID) @Positive Long userId
    ) {
        log.info("Requesting booking with ID: {}", id);
        return ResponseEntity.ok(client.get(id, userId));
    }

    @GetMapping
    @Operation(summary = "Get all bookings for specific booker")
    public ResponseEntity<Object> findBookingsByBooker(
            @RequestHeader(HEADER_USER_ID) @Positive Long bookerId,
            @RequestParam(defaultValue = "ALL") State state,
            @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(defaultValue = "10") @Positive Integer size
    ) {
        log.info("Getting {} bookings of user with ID: {}", state, bookerId);
        return client.get(bookerId, state, from, size);
    }

    @GetMapping("/owner")
    @Operation(summary = "Get all bookings for specific owner")
    public ResponseEntity<Object> findBookingsByOwner(
            @RequestHeader(HEADER_USER_ID) @Positive Long ownerId,
            @RequestParam(defaultValue = "ALL") State state,
            @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(defaultValue = "10") @Positive Integer size
    ) {
        log.info("Getting {} bookings of user with ID: {}", state, ownerId);
        return client.getBookingsForYourItems(ownerId, state, from, size);
    }

    @PostMapping
    @Operation(summary = "Create an item booking request")
    public ResponseEntity<Object> createBooking(
            @RequestBody @Valid BookingCreateRequest bookingCreateRequest,
            @RequestHeader(HEADER_USER_ID) @Positive Long bookerId
    ) {
        log.info("User with ID: {} requesting booking for item with ID:{}", bookerId, bookingCreateRequest.getItemId());
        return client.post(bookingCreateRequest, bookerId);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Confirm item booking")
    public ResponseEntity<Object> confirmBooking(
            @PathVariable @Positive Long id,
            @RequestHeader(HEADER_USER_ID) @Positive Long userId,
            @RequestParam Boolean approved
    ) {
        log.info("User with ID: {} is confirming booking with ID: {}, approved: {}", userId, id, approved);
        return ResponseEntity.ok(client.patch(id, userId, approved));
    }
}
