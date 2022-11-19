package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dao.ItemDao;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.exceptions.ItemNotFoundException;
import ru.practicum.shareit.item.exceptions.WrongUserException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserDao;
import ru.practicum.shareit.user.exceptions.UserNotFoundException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemDao itemRepository;
    private final UserDao userRepository;
    private final Validator validator;

    @Override
    public ItemDto findItem(Long id) {
        return ItemMapper.INSTANCE.itemToItemDto(getItem(id));
    }

    @Override
    public List<ItemDto> findAllItems(Long userId) {
        return itemRepository.findAllItems(userId)
                .stream()
                .map(ItemMapper.INSTANCE::itemToItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto createItem(ItemDto itemDto, Long ownerId) {
        Item newItem = ItemMapper.INSTANCE.itemDtoToItem(itemDto);
        newItem.setOwner(userRepository
                .findUser(ownerId)
                .orElseThrow(() -> new UserNotFoundException("No user with ID: " + ownerId)));
        return ItemMapper.INSTANCE.itemToItemDto(itemRepository.createItem(newItem));
    }

    @Override
    public ItemDto updateItem(ItemDto partialItemDto, Long itemId, Long ownerId) {
        Item targetItem = getItem(itemId);
        Item updatedItem = targetItem.toBuilder().build();
        if (updatedItem.getOwner().getId().equals(ownerId)) {
            ItemMapper.INSTANCE.updateItemFromDto(partialItemDto, updatedItem);
            Set<ConstraintViolation<Item>> violations = validator.validate(updatedItem);
            if (violations.isEmpty()) {
                return ItemMapper.INSTANCE.itemToItemDto(itemRepository.updateItem(updatedItem));
            } else {
                throw new ConstraintViolationException(violations);
            }
        } else {
            throw new WrongUserException("Item does not belong to user with ID: " + ownerId);
        }
    }

    @Override
    public void deleteItem(Long id) {
        findItem(id);
        itemRepository.deleteItem(id);
    }

    @Override
    public List<ItemDto> searchItems(String text) {
        return itemRepository.searchItems(text)
                .stream()
                .map(ItemMapper.INSTANCE::itemToItemDto)
                .collect(Collectors.toList());
    }

    private Item getItem(Long id) {
        return itemRepository.findItem(id).orElseThrow(() -> new ItemNotFoundException("No item with ID: " + id));
    }
}
