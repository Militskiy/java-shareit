package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemListDto;

public interface ItemService {
    ItemDto findItem(Long id);

    ItemListDto findAllItems(Long userId);

    ItemDto createItem(ItemDto item, Long ownerId);

    ItemDto updateItem(ItemDto item, Long itemId, Long ownerId);

    void deleteItem(Long id);

    ItemListDto searchItems(String text);
}
