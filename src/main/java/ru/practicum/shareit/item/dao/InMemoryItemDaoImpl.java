package ru.practicum.shareit.item.dao;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class InMemoryItemDaoImpl implements ItemDao {
    private long idCounter = 0;

    private final Map<Long, Item> items = new LinkedHashMap<>();

    @Override
    public Optional<Item> findItem(Long id) {
        return Optional.ofNullable(items.get(id));
    }

    @Override
    public List<Item> findAllItems(Long userId) {
        return items.values()
                .stream()
                .filter(item -> item.getOwner().getId().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public Item createItem(Item item) {
        item.setId(++idCounter);
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Item updateItem(Item item) {
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public void deleteItem(Long id) {
        items.remove(id);
    }

    @Override
    public List<Item> searchItems(String text) {
        return items.values()
                .stream()
                .filter(item -> item.getDescription().toUpperCase().contains(text))
                .filter(item -> item.getAvailable().equals(Boolean.TRUE))
                .collect(Collectors.toList());
    }
}
