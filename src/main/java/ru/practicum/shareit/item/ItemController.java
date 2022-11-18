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
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.exceptions.WrongUserException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/items")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Item services")
public class ItemController {
    private final ItemService itemService;

    @GetMapping
    @Operation(summary = "Get a list of all items that belong to specific user")
    public ResponseEntity<List<ItemDto>> findAllItems(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Getting a list of all items that belong to user with ID: " + userId);
        return ResponseEntity.ok(itemService.findAllItems(userId)
                .stream()
                .map(ItemMapper.INSTANCE::itemToItemDto)
                .collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get an item")
    public ResponseEntity<ItemDto> findItem(
            @RequestHeader("X-Sharer-User-Id") @Min(1) Long userId,
            @PathVariable @Min(1) Long id
    ) {
        log.info("Getting item with ID: " + id + " that belongs to user " + userId);
        return ResponseEntity.ok(ItemMapper.INSTANCE.itemToItemDto(itemService.findItem(id)));
    }

    @GetMapping("/search")
    @Operation(summary = "Search for items")
    public ResponseEntity<List<ItemDto>> searchForItems(@RequestParam String text) {
        log.info("Searching for items, keyword: " + text);
        if (!text.isBlank()) {
            return ResponseEntity.ok(itemService.searchItems(text.toUpperCase())
                    .stream()
                    .map(ItemMapper.INSTANCE::itemToItemDto)
                    .collect(Collectors.toList()));
        } else {
            return ResponseEntity.ok(new ArrayList<>());
        }
    }

    @PostMapping
    @Operation(summary = "Add a new item")
    public ResponseEntity<ItemDto> createItem(
            @RequestHeader("X-Sharer-User-Id") @Min(1) Long userId,
            @RequestBody @Valid ItemDto itemDto
    ) {
        log.info("Creating item: " + itemDto + " for user with ID: " + userId);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        ItemMapper.INSTANCE.itemToItemDto(
                                itemService.createItem(ItemMapper.INSTANCE.itemDtoToItem(itemDto), userId)
                        )
                );
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update an item")
    public ResponseEntity<ItemDto> updateItem(
            @RequestHeader("X-Sharer-User-Id") @Min(1) Long userId,
            @PathVariable @Min(1) Long id,
            @RequestBody ItemDto itemDto
    ) {
        log.info("Updating item with ID: " + id + " that belongs to user with ID: " + userId);
        Item item = itemService.findItem(id);
        if (item.getOwner().getId().equals(userId)) {
            ItemMapper.INSTANCE.updateItemFromDto(itemDto, item);
            return ResponseEntity.ok().body(ItemMapper.INSTANCE.itemToItemDto(itemService.updateItem(item)));
        } else {
            throw new WrongUserException("Item does not belong to user with ID: " + item.getOwner().getId());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an item")
    public void deleteItem(
            @RequestHeader("X-Sharer-User-Id") @Min(1) Long userId,
            @PathVariable @Min(1) Long id
    ) {
        log.info("Deleting item with ID: " + id + " that belongs to user with ID: " + userId);
        itemService.deleteItem(id);
    }
}
