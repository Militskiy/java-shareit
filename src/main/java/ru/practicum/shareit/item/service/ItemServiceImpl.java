package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemListDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.dto.ResponseItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;
import ru.practicum.shareit.item.exceptions.ItemNotFoundException;
import ru.practicum.shareit.item.exceptions.WrongUserException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.exceptions.UserNotFoundException;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ItemMapper itemMapper;

    @Override
    public ResponseItemDto findItem(Long id) {
        return itemMapper.itemToResponseItemDto(getItem(id));
    }

    @Override
    public ItemListDto findAllItems(Long ownerId, PageRequest pageRequest) {
        if (userRepository.existsById(ownerId)) {
            return ItemListDto.builder()
                    .items(itemMapper.map(itemRepository.findAllByOwnerId(ownerId, pageRequest)))
                    .build();
        } else {
            throw new UserNotFoundException("No user with ID: " + ownerId);
        }
    }

    @Override
    @Transactional
    public ResponseItemDto createItem(ItemDto itemDto, Long ownerId) {
        Item newItem = itemMapper.itemDtoToItem(itemDto);
        newItem.setOwner(userRepository
                .findById(ownerId)
                .orElseThrow(() -> new UserNotFoundException("No user with ID: " + ownerId)));
        return itemMapper.itemToResponseItemDto(itemRepository.save(newItem));
    }

    @Override
    @Transactional
    public ResponseItemDto updateItem(UpdateItemDto updateItemDto, Long itemId, Long ownerId) {
        Item targetItem = getItem(itemId);
        if (targetItem.getOwner().getId().equals(ownerId)) {
            return itemMapper.itemToResponseItemDto(
                    itemRepository.save(itemMapper.updateItemFromUpdateItemDto(updateItemDto, targetItem))
            );
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
                    .items(itemMapper.map(
                            itemRepository.findAllByDescriptionContainingIgnoreCaseAndAvailableIsTrue(text, pageRequest)
                    ))
                    .build();
        } else {
            return ItemListDto.builder().items(new ArrayList<>()).build();
        }
    }

    private Item getItem(Long id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("No item with ID: " + id));
    }
}
