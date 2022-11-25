package ru.practicum.shareit.item;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemListDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import javax.validation.constraints.Min;


@RestController
@RequestMapping("/items")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Item services")
public class ItemController {
    private final ItemService itemService;
    private final static String HEADER_OWNER_ID = "X-Sharer-User-Id";

    @GetMapping
    @Operation(summary = "Get a list of all items that belong to specific user")
    public ResponseEntity<ItemListDto> findAllItems(@RequestHeader(HEADER_OWNER_ID) Long userId) {
        log.info("Getting a list of all items that belong to user with ID: " + userId);
        return ResponseEntity.ok(itemService.findAllItems(userId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get an item")
    public ResponseEntity<ItemDto> findItem(
            @PathVariable @Min(1) Long id
    ) {
        log.info("Getting item with ID: " + id);
        return ResponseEntity.ok(itemService.findItem(id));
    }

    @GetMapping("/search")
    @Operation(summary = "Search for items")
    public ResponseEntity<ItemListDto> searchForItems(@RequestParam String text) {
        log.info("Searching for items, keyword: " + text);
        return ResponseEntity.ok(itemService.searchItems(text.toUpperCase()));
    }

    @PostMapping
    @Operation(summary = "Add a new item")
    public ResponseEntity<ItemDto> createItem(
            @RequestHeader(HEADER_OWNER_ID) @Min(1) Long userId,
            @RequestBody @Valid ItemDto itemDto
    ) {
        log.info("Creating item: " + itemDto + " for user with ID: " + userId);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body((itemService.createItem(itemDto, userId)));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update an item")
    public ResponseEntity<ItemDto> updateItem(
            @RequestHeader(HEADER_OWNER_ID) @Min(1) Long ownerId,
            @PathVariable @Min(1) Long id,
            @RequestBody ItemDto itemDto
    ) {
        log.info("Updating item with ID: " + id + " that belongs to user with ID: " + ownerId);
        return ResponseEntity.ok().body((itemService.updateItem(itemDto, id, ownerId)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an item")
    public void deleteItem(
            @RequestHeader(HEADER_OWNER_ID) @Min(1) Long userId,
            @PathVariable @Min(1) Long id
    ) {
        log.info("Deleting item with ID: " + id + " that belongs to user with ID: " + userId);
        itemService.deleteItem(id);
    }
}
