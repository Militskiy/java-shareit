package ru.practicum.shareit.request;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.request.client.ItemRequestClient;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import static ru.practicum.shareit.BaseClient.HEADER_USER_ID;

@RestController
@RequestMapping(path = "/requests")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Item request service")
@Validated
public class ItemRequestController {
    private final ItemRequestClient client;

    @PostMapping
    @Operation(summary = "Create an item request")
    public ResponseEntity<Object> createItemRequest(
            @RequestHeader(HEADER_USER_ID) @Positive Long userId,
            @RequestBody @Valid ItemRequestCreateDto itemRequestCreateDto
    ) {
        log.info("Creating item request with description: {}", itemRequestCreateDto.getDescription());
        return client.post(itemRequestCreateDto, userId);
    }

    @GetMapping
    @Operation(summary = "Get own item requests")
    public ResponseEntity<Object> findOwnRequests(
            @RequestHeader(HEADER_USER_ID) @Positive Long userId,
            @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(defaultValue = "10") @Positive Integer size
    ) {
        log.info("Getting a list of item requests made by user with ID: {}", userId);
        return client.get(userId, from, size);
    }

    @GetMapping("/all")
    @Operation(summary = "Get all item requests")
    public ResponseEntity<Object> findAllRequests(
            @RequestHeader(HEADER_USER_ID) @Positive Long userId,
            @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(defaultValue = "10") @Positive Integer size
    ) {
        log.info("Getting a list of all item requests");
        return client.getAllRequests(userId, from, size);
    }

    @GetMapping("/{requestId}")
    @Operation(summary = "Get request by ID")
    public ResponseEntity<Object> findById(
            @RequestHeader(HEADER_USER_ID) @Positive Long userId,
            @PathVariable @Positive Long requestId
    ) {
        log.info("Getting item request with ID: {}", requestId);
        return client.get(userId, requestId);
    }
}
