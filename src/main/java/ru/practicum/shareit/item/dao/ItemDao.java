package ru.practicum.shareit.item.dao;

import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemDao {
    Optional<Item> findItem(Long id);

    List<Item> findAllItems(Long userId);

    Item createItem(Item item);

    Item updateItem(Item item);

    void deleteItem(Long id);

    List<Item> searchItems(String text);
}
