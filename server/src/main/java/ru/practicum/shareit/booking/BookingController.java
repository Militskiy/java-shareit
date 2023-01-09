package ru.practicum.shareit.booking;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingListDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.service.BookingService;

import static ru.practicum.shareit.util.Convert.toPageRequest;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Booking services")
public class BookingController {

    private static final String HEADER_USER_ID = "X-Sharer-User-Id";
    private final BookingService bookingService;

    @GetMapping("/{id}")
    @Operation(summary = "Get booking information")
    public ResponseEntity<BookingResponseDto> findBooking(
            @PathVariable Long id,
            @RequestHeader(HEADER_USER_ID) Long userId
    ) {
        log.info("Requesting booking with ID: {}", id);
        return ResponseEntity.ok(bookingService.findBooking(id, userId));
    }

    @GetMapping
    @Operation(summary = "Get all bookings for specific booker")
    public ResponseEntity<BookingListDto> findBookingsByBooker(
            @RequestHeader(HEADER_USER_ID) Long bookerId,
            @RequestParam(defaultValue = "ALL") State state,
            @RequestParam(defaultValue = "0") Integer from,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        log.info("Getting {} bookings of user with ID: {}", state, bookerId);
        return ResponseEntity.ok(
                bookingService.findBookingsByBookerAndState(bookerId, state, toPageRequest(from, size))
        );
    }

    @GetMapping("/owner")
    @Operation(summary = "Get all bookings for specific owner")
    public ResponseEntity<BookingListDto> findBookingsByOwner(
            @RequestHeader(HEADER_USER_ID) Long ownerId,
            @RequestParam(defaultValue = "ALL") State state,
            @RequestParam(defaultValue = "0") Integer from,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        log.info("Getting {} bookings of user with ID: {}", state, ownerId);
        return ResponseEntity.ok(
                bookingService.findBookingsByOwnerAndState(ownerId, state, toPageRequest(from, size))
        );
    }

    @PostMapping
    @Operation(summary = "Create an item booking request")
    public ResponseEntity<BookingResponseDto> createBooking(
            @RequestBody BookingCreateDto bookingCreateDto,
            @RequestHeader(HEADER_USER_ID) Long bookerId
    ) {
        log.info("User with ID: {} requesting booking for item with ID:{}", bookerId, bookingCreateDto.getItemId());
        return ResponseEntity.status(HttpStatus.CREATED).body(bookingService.createBooking(bookingCreateDto, bookerId));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Confirm item booking")
    public ResponseEntity<BookingResponseDto> confirmBooking(
            @PathVariable Long id,
            @RequestHeader(HEADER_USER_ID) Long userId,
            @RequestParam Boolean approved
    ) {
        log.info("User with ID: {} is confirming booking with ID: {}, approved: {}", userId, id, approved);
        return ResponseEntity.ok(bookingService.confirmBooking(id, userId, approved));
    }
}
