package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto findItem(Long id);

    List<ItemDto> findAllItems(Long userId);

    ItemDto createItem(ItemDto item, Long ownerId);

    ItemDto updateItem(ItemDto item, Long itemId, Long ownerId);

    void deleteItem(Long id);

    List<ItemDto> searchItems(String text);
}
