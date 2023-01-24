package ru.practicum.shareit.item;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.item.client.ItemClient;
import ru.practicum.shareit.item.dto.CommentCreateRequest;
import ru.practicum.shareit.item.dto.ItemCreateRequest;
import ru.practicum.shareit.item.dto.ItemUpdateRequest;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import static ru.practicum.shareit.BaseClient.HEADER_USER_ID;

@RestController
@RequestMapping(path = "/items")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Item services")
@Validated
public class ItemController {
    private final ItemClient client;

    @GetMapping
    @Operation(summary = "Get a list of all items that belong to specific user")
    public ResponseEntity<Object> findAllItems(
            @RequestHeader(HEADER_USER_ID) @Positive Long userId,
            @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(defaultValue = "10") @Positive Integer size
    ) {
        log.info("Getting a list of all items that belong to user with ID: " + userId);
        return client.get(userId, from, size);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get an item")
    public ResponseEntity<Object> findItem(
            @RequestHeader(HEADER_USER_ID) @Positive Long userId,
            @PathVariable @Min(1) Long id
    ) {
        log.info("Getting item with ID: " + id);
        return ResponseEntity.ok(client.get(userId, id));
    }

    @GetMapping("/search")
    @Operation(summary = "Search for items")
    public ResponseEntity<Object> searchForItems(
            @RequestParam String text,
            @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(defaultValue = "10") @Positive Integer size
    ) {
        log.info("Searching for items, keyword: " + text);
        return client.search(text, from, size);
    }

    @PostMapping
    @Operation(summary = "Add a new item")
    public ResponseEntity<Object> createItem(
            @RequestHeader(HEADER_USER_ID) @Positive Long userId,
            @RequestBody @Valid ItemCreateRequest itemCreateRequest
    ) {
        log.info("Creating item: " + itemCreateRequest + " for user with ID: " + userId);
        return client.post(itemCreateRequest, userId);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update an item")
    public ResponseEntity<Object> updateItem(
            @RequestHeader(HEADER_USER_ID) @Positive Long ownerId,
            @PathVariable @Min(1) Long id,
            @RequestBody @Valid ItemUpdateRequest itemUpdateRequest
    ) {
        log.info("Updating item with ID: " + id + " that belongs to user with ID: " + ownerId);
        return ResponseEntity.ok(client.patch(itemUpdateRequest, ownerId, id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an item")
    public ResponseEntity<Void> deleteItem(
            @RequestHeader(HEADER_USER_ID) @Positive Long userId,
            @PathVariable @Positive Long id
    ) {
        log.info("Deleting item with ID: " + id + " that belongs to user with ID: " + userId);
        client.delete(userId, id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/comment")
    @Operation(summary = "Leave an item comment")
    public ResponseEntity<Object> commentItem(
            @RequestHeader(HEADER_USER_ID) @Positive Long userId,
            @PathVariable @Positive Long id,
            @RequestBody @Valid CommentCreateRequest comment
    ) {
        log.info("User with ID: {} is commenting on item with ID: {}", userId, id);
        return client.comment(userId, id, comment);
    }

}
