package ru.practicum.shareit.item.service;

import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemListDto;
import ru.practicum.shareit.item.dto.ItemListWithBookingsDto;
import ru.practicum.shareit.item.dto.ItemWithBookingsDto;
import ru.practicum.shareit.item.dto.ResponseItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;

public interface ItemService {
    ItemWithBookingsDto findItem(Long id, Long userId);

    ItemListWithBookingsDto findAllItems(Long userId, PageRequest pageRequest);

    ResponseItemDto createItem(ItemDto item, Long ownerId);

    ResponseItemDto updateItem(UpdateItemDto updateItemDto, Long itemId, Long ownerId);

    void deleteItem(Long id);

    ItemListDto searchItems(String text, PageRequest pageRequest);
}
