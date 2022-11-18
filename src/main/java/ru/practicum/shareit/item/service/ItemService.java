package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    Item findItem(Long id);

    List<Item> findAllItems(Long userId);

    Item createItem(Item item, Long ownerId);

    Item updateItem(Item item);

    void deleteItem(Long id);

    List<Item> searchItems(String text);
}
