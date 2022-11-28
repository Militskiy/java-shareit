package ru.practicum.shareit.item.service;

import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemListDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;

public interface ItemService {
    ItemDto findItem(Long id);

    ItemListDto findAllItems(Long userId, PageRequest pageRequest);

    ItemDto createItem(ItemDto item, Long ownerId);

    ItemDto updateItem(UpdateItemDto updateItemDto, Long itemId, Long ownerId);

    void deleteItem(Long id);

    ItemListDto searchItems(String text, PageRequest pageRequest);
}
