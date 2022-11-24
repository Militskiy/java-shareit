package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
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
import ru.practicum.shareit.user.model.User;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;

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
    public ItemListDto findAllItems(Long userId) {
        ExampleMatcher searchByUserIdExampleMatcher = ExampleMatcher.matchingAny()
                .withMatcher("owner", ExampleMatcher.GenericPropertyMatchers.exact())
                .withIgnorePaths("id", "name", "description", "available", "request");
        Example<Item> example = Example.of(
                Item.builder()
                        .owner(User.builder().id(userId).build())
                        .build(), searchByUserIdExampleMatcher
        );
        return ItemListDto.builder()
                .itemDtoList(
                        itemRepository.findAll(example)
                                .stream()
                                .map(itemMapper::itemToItemDto)
                                .collect(Collectors.toList())
                ).build();
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

    public ItemListDto searchItems(String text) {
        if (!text.isBlank()) {
            ExampleMatcher caseInsensitiveSearch = ExampleMatcher.matching()
                    .withMatcher("description", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                    .withMatcher("available", ExampleMatcher.GenericPropertyMatchers.exact())
                    .withIgnorePaths("id", "name", "owner", "request");
            Example<Item> example = Example.of(
                    Item.builder()
                            .description(text)
                            .available(Boolean.TRUE)
                            .build(), caseInsensitiveSearch
            );
            return ItemListDto.builder()
                    .itemDtoList(
                            itemRepository.findAll(example)
                                    .stream()
                                    .map(itemMapper::itemToItemDto)
                                    .collect(Collectors.toList())
                    ).build();
        } else {
            return ItemListDto.builder().itemDtoList(new ArrayList<>()).build();
        }
    }

    private Item getItem(Long id) {
        return itemRepository.findById(id).orElseThrow(() -> new ItemNotFoundException("No item with ID: " + id));
    }
}
