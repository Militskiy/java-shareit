package ru.practicum.shareit.request;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestCreateResponseDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDtoList;
import ru.practicum.shareit.request.service.ItemRequestService;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/requests")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Item request service")
public class ItemRequestController {
    private static final String HEADER_USER_ID = "X-Sharer-User-Id";
    private final ItemRequestService itemRequestService;

    @PostMapping
    @Operation(summary = "Create an item request")
    public ResponseEntity<ItemRequestCreateResponseDto> createItemRequest(
            @RequestHeader(HEADER_USER_ID) Long userId,
            @RequestBody @Valid ItemRequestCreateDto itemRequestCreateDto
    ) {
        log.info("Creating item request with description: {}", itemRequestCreateDto.getDescription());
        return ResponseEntity.ok(itemRequestService.create(itemRequestCreateDto, userId));
    }

    @GetMapping
    @Operation(summary = "Get owm item requests")
    public ResponseEntity<ItemRequestResponseDtoList> findOwnRequests(
            @RequestHeader(HEADER_USER_ID) Long userId,
            @RequestParam(defaultValue = "0") Integer from,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        log.info("Getting a list of item requests made by user with ID: {}", userId);
        return ResponseEntity.ok(itemRequestService.findOwnRequests(
                userId,
                PageRequest.of(from, size, Sort.by("created").descending())
        ));
    }

    @GetMapping("/all")
    @Operation(summary = "Get all item requests")
    public ResponseEntity<ItemRequestResponseDtoList> findAllRequests(
            @RequestHeader(HEADER_USER_ID) long userId,
            @RequestParam(defaultValue = "0") Integer from,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        log.info("Getting a list of all item requests");
        return ResponseEntity.ok(itemRequestService.findAllRequests(
                userId,
                PageRequest.of(from, size, Sort.by("created").descending())
        ));
    }

    @GetMapping("/{requestId}")
    @Operation(summary = "Get request by ID")
    public ResponseEntity<ItemRequestResponseDto> findById(
            @RequestHeader(HEADER_USER_ID) Long userId,
            @PathVariable Long requestId
    ) {
        log.info("Getting item request with ID: {}", requestId);
        return ResponseEntity.ok(itemRequestService.findById(requestId, userId));
    }
}
