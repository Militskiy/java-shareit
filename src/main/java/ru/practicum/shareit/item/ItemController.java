package ru.practicum.shareit.item;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
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
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemListDto;
import ru.practicum.shareit.item.dto.ItemListWithBookingsDto;
import ru.practicum.shareit.item.dto.ItemWithBookingsDto;
import ru.practicum.shareit.item.dto.ResponseItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import static ru.practicum.shareit.util.Convert.toPageRequest;


@RestController
@RequestMapping("/items")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Item services")
@Validated
public class ItemController {
    private static final String HEADER_USER_ID = "X-Sharer-User-Id";
    private final ItemService itemService;

    @GetMapping
    @Operation(summary = "Get a list of all items that belong to specific user")
    public ResponseEntity<ItemListWithBookingsDto> findAllItems(
            @RequestHeader(HEADER_USER_ID) @Positive Long userId,
            @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(defaultValue = "10") @Positive Integer size
    ) {
        log.info("Getting a list of all items that belong to user with ID: " + userId);
        return ResponseEntity.ok(itemService.findAllItems(
                userId, toPageRequest(from, size).withSort(Sort.by("id").ascending())
        ));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get an item")
    public ResponseEntity<ItemWithBookingsDto> findItem(
            @RequestHeader(HEADER_USER_ID) @Positive Long userId,
            @PathVariable @Min(1) Long id
    ) {
        log.info("Getting item with ID: " + id);
        return ResponseEntity.ok(itemService.findItem(id, userId));
    }

    @GetMapping("/search")
    @Operation(summary = "Search for items")
    public ResponseEntity<ItemListDto> searchForItems(
            @RequestParam String text,
            @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(defaultValue = "10") @Positive Integer size
    ) {
        log.info("Searching for items, keyword: " + text);
        return ResponseEntity.ok(itemService.searchItems(
                text.toUpperCase(), toPageRequest(from, size).withSort(Sort.by("id").ascending())
        ));
    }

    @PostMapping
    @Operation(summary = "Add a new item")
    public ResponseEntity<ResponseItemDto> createItem(
            @RequestHeader(HEADER_USER_ID) @Positive Long userId,
            @RequestBody @Valid ItemDto itemDto
    ) {
        log.info("Creating item: " + itemDto + " for user with ID: " + userId);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body((itemService.createItem(itemDto, userId)));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update an item")
    public ResponseEntity<ResponseItemDto> updateItem(
            @RequestHeader(HEADER_USER_ID) @Positive Long ownerId,
            @PathVariable @Min(1) Long id,
            @RequestBody @Valid UpdateItemDto updateItemDto
    ) {
        log.info("Updating item with ID: " + id + " that belongs to user with ID: " + ownerId);
        return ResponseEntity.ok().body((itemService.updateItem(updateItemDto, id, ownerId)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an item")
    public ResponseEntity<Void> deleteItem(
            @RequestHeader(HEADER_USER_ID) @Positive Long userId,
            @PathVariable @Positive Long id
    ) {
        log.info("Deleting item with ID: " + id + " that belongs to user with ID: " + userId);
        itemService.deleteItem(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/comment")
    @Operation(summary = "Leave an item comment")
    public ResponseEntity<CommentResponseDto> commentItem(
            @RequestHeader(HEADER_USER_ID) @Positive Long userId,
            @PathVariable @Positive Long id,
            @RequestBody @Valid CommentCreateDto comment
    ) {
        log.info("User with ID: {} is commenting on item with ID: {}", userId, id);
        return ResponseEntity.ok(itemService.commentItem(userId, id, comment));
    }
}
