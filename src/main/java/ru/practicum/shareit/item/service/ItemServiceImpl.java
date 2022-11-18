package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dao.ItemDao;
import ru.practicum.shareit.item.exceptions.ItemNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemDao itemRepository;
    private final UserService userService;
    private final Validator validator;

    @Override
    public Item findItem(Long id) {
        return itemRepository.findItem(id).orElseThrow(() -> new ItemNotFoundException("No item with ID: " + id));
    }

    @Override
    public List<Item> findAllItems(Long userId) {
        return itemRepository.findAllItems(userId);
    }

    @Override
    public Item createItem(Item item, Long ownerId) {
        item.setOwner(userService.findUser(ownerId));
        return itemRepository.createItem(item);
    }

    @Override
    public Item updateItem(Item item) {
        Set<ConstraintViolation<Item>> violations = validator.validate(item);
        if (violations.isEmpty()) {
            return itemRepository.updateItem(item);
        } else {
            throw new ConstraintViolationException(violations);
        }
    }

    @Override
    public void deleteItem(Long id) {
        findItem(id);
        itemRepository.deleteItem(id);
    }

    @Override
    public List<Item> searchItems(String text) {
        return itemRepository.searchItems(text);
    }
}
