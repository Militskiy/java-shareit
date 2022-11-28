package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemListDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.exceptions.ItemNotFoundException;
import ru.practicum.shareit.item.exceptions.WrongUserException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.exceptions.UserNotFoundException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final Validator validator;
    private final ItemMapper itemMapper;

    @Override
    public ItemDto findItem(Long id) {
        return itemMapper.itemToItemDto(getItem(id));
    }

    @Override
    public ItemListDto findAllItems(Long ownerId, PageRequest pageRequest) {
        if (userRepository.existsById(ownerId)) {
            return ItemListDto.builder()
                    .itemDtoList(itemMapper.map(itemRepository.findAllByOwnerId(ownerId, pageRequest)))
                    .build();
        } else {
            throw new UserNotFoundException("No user with ID: " + ownerId);
        }
    }

    @Override
    @Transactional
    public ItemDto createItem(ItemDto itemDto, Long ownerId) {
        Item newItem = itemMapper.itemDtoToItem(itemDto);
        newItem.setOwner(userRepository
                .findById(ownerId)
                .orElseThrow(() -> new UserNotFoundException("No user with ID: " + ownerId)));
        return itemMapper.itemToItemDto(itemRepository.save(newItem));
    }

    @Override
    @Transactional
    public ItemDto updateItem(ItemDto partialItemDto, Long itemId, Long ownerId) {
        Item targetItem = getItem(itemId);
        Item updatedItem = targetItem.toBuilder().build();
        if (updatedItem.getOwner().getId().equals(ownerId)) {
            itemMapper.updateItemFromDto(partialItemDto, updatedItem);
            Set<ConstraintViolation<Item>> violations = validator.validate(updatedItem);
            if (violations.isEmpty()) {
                return itemMapper.itemToItemDto(itemRepository.save(updatedItem));
            } else {
                throw new ConstraintViolationException(violations);
            }
        } else {
            throw new WrongUserException("Item does not belong to user with ID: " + ownerId);
        }
    }

    @Override
    @Transactional
    public void deleteItem(Long id) {
        itemRepository.deleteById(id);
    }

    @Override
    public ItemListDto searchItems(String text, PageRequest pageRequest) {
        if (!text.isBlank()) {
            return ItemListDto.builder()
                    .itemDtoList(itemMapper.map(
                            itemRepository.findAllByDescriptionContainingIgnoreCaseAndAvailableIsTrue(text, pageRequest)
                    ))
                    .build();
        } else {
            return ItemListDto.builder().itemDtoList(new ArrayList<>()).build();
        }
    }

    private Item getItem(Long id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("No item with ID: " + id));
    }
}
