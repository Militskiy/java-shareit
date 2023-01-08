package ru.practicum.shareit.booking.client;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.shareit.BaseClient;
import ru.practicum.shareit.booking.dto.BookingCreateRequest;
import ru.practicum.shareit.booking.dto.State;

import javax.validation.constraints.Positive;

@FeignClient(value = "bookings", path = "/bookings", url = "${shareit-server.url}")
public interface BookingClient extends BaseClient<BookingCreateRequest> {
    @Cacheable(cacheNames = "bookings", key = "#id")
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<Object> get(
            @PathVariable("id") Long id,
            @RequestHeader(HEADER_USER_ID) Long userId
    );

    @Caching(evict = {
            @CacheEvict(cacheNames = "bookings", key = "#id"),
            @CacheEvict(cacheNames = "items", allEntries = true)
    }
    )
    @RequestMapping(
            method = RequestMethod.PATCH,
            value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<Object> patch(
            @PathVariable Long id,
            @RequestHeader(HEADER_USER_ID) @Positive Long userId,
            @RequestParam Boolean approved
    );

    @RequestMapping(
            method = RequestMethod.GET,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<Object> get(
            @RequestHeader(HEADER_USER_ID) Long bookerId,
            @RequestParam State state,
            @RequestParam Integer from,
            @RequestParam Integer size
    );

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/owner",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<Object> getBookingsForYourItems(
            @RequestHeader(HEADER_USER_ID) Long ownerId,
            @RequestParam State state,
            @RequestParam Integer from,
            @RequestParam Integer size
    );

    @Override
    @CacheEvict(cacheNames = "items", key = "#createRequest.itemId")
    @RequestMapping(
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<Object> post(
            @RequestBody BookingCreateRequest createRequest,
            @RequestHeader(HEADER_USER_ID) Long bookerId
    );
}
