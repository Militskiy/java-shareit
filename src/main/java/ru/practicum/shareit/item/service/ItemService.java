package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoList;

public interface ItemService {
    ItemDto findItem(Long id);

    ItemDtoList findAllItems(Long userId);

    ItemDto createItem(ItemDto item, Long ownerId);

    ItemDto updateItem(ItemDto item, Long itemId, Long ownerId);

    void deleteItem(Long id);

    ItemDtoList searchItems(String text);
}
