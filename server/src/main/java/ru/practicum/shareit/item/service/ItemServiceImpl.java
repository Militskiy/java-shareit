package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dao.CommentRepository;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.CommentMapper;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemListDto;
import ru.practicum.shareit.item.dto.ItemListWithBookingsDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.dto.ItemWithBookingsDto;
import ru.practicum.shareit.item.dto.ResponseItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;
import ru.practicum.shareit.item.exceptions.CommentException;
import ru.practicum.shareit.item.exceptions.WrongUserException;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final ItemMapper itemMapper;
    private final BookingMapper bookingMapper;
    private final CommentMapper commentMapper;


    @Override
    @Transactional(readOnly = true)
    public ItemWithBookingsDto findItem(Long id, Long userId) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("No item with ID: " + id));
        ItemWithBookingsDto itemDto = itemMapper.itemToItemWithBookingsDto(item);
        if (item.getOwner().getId().equals(userId)) {
            BookingShortDto lastBooking = bookingMapper.bookingToBookingShortDto(
                    bookingRepository.findFirstByItem_IdAndStatusAndStartIsBeforeOrderByStartDesc(
                            id, Status.APPROVED, LocalDateTime.now()
                    ).orElse(null)
            );
            BookingShortDto nextBooking = bookingMapper.bookingToBookingShortDto(
                    bookingRepository.findFirstByItem_IdAndStatusAndStartIsAfterOrderByStartAsc(
                            id, Status.APPROVED, LocalDateTime.now()
                    ).orElse(null)
            );
            itemDto.setLastBooking(lastBooking);
            itemDto.setNextBooking(nextBooking);
        }
        return itemDto;
    }

    @Override
    @Transactional(readOnly = true)
    public ItemListWithBookingsDto findAllItems(Long ownerId, PageRequest pageRequest) {
        if (userRepository.existsById(ownerId)) {
            return ItemListWithBookingsDto.builder()
                    .items(
                            itemMapper.mapWithBooking(
                                            itemRepository.findAllByOwnerId(ownerId, pageRequest)
                                    )
                                    .stream()
                                    .peek(item -> {
                                        item.setLastBooking(
                                                bookingMapper.bookingToBookingShortDto(bookingRepository
                                                        .findFirstByItem_IdAndStatusAndStartIsBeforeOrderByStartDesc(
                                                                item.getId(), Status.APPROVED, LocalDateTime.now()
                                                        ).orElse(null)
                                                )
                                        );
                                        item.setNextBooking(
                                                bookingMapper.bookingToBookingShortDto(bookingRepository
                                                        .findFirstByItem_IdAndStatusAndStartIsAfterOrderByStartAsc(
                                                                item.getId(), Status.APPROVED, LocalDateTime.now()
                                                        ).orElse(null)
                                                )
                                        );
                                    })
                                    .collect(Collectors.toList())
                    )
                    .build();
        } else {
            throw new NotFoundException("No user with ID: " + ownerId);
        }
    }

    @Override
    public ResponseItemDto createItem(ItemDto itemDto, Long ownerId) {
        Item newItem = itemMapper.itemDtoToItem(itemDto);
        if (newItem.getRequest().getId() == null) {
            newItem.setRequest(null);
        }
        newItem.setOwner(userRepository
                .findById(ownerId)
                .orElseThrow(() -> new NotFoundException("No user with ID: " + ownerId)));
        return itemMapper.itemToResponseItemDto(itemRepository.save(newItem));
    }

    @Override
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
    public void deleteItem(Long id) {
        itemRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public ItemListDto searchItems(String text, PageRequest pageRequest) {
        if (!text.isBlank()) {
            return ItemListDto.builder()
                    .items(itemMapper.mapResponse(
                            itemRepository.findAllByDescriptionContainingIgnoreCaseAndAvailableIsTrue(text, pageRequest)
                    ))
                    .build();
        } else {
            return ItemListDto.builder().items(new ArrayList<>()).build();
        }
    }

    @Override
    public CommentResponseDto commentItem(Long userId, Long itemId, CommentCreateDto commentDto) {
        if (bookingRepository.existsBookingByItem_IdAndBooker_IdAndStatusAndEndIsBefore(
                itemId, userId, Status.APPROVED, LocalDateTime.now()
        )) {
            Item item = getItem(itemId);
            User user = userRepository.getReferenceById(userId);
            Comment comment = commentMapper.commentCreateDtoToComment(commentDto);
            comment.setItem(item);
            comment.setAuthor(user);
            return commentMapper.commentToCommentResponseDto(commentRepository.save(comment));
        } else {
            throw new CommentException("Cannot comment this item");
        }
    }

    private Item getItem(Long id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("No item with ID: " + id));
    }
}
