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
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final ItemMapper itemMapper;
    private final BookingMapper bookingMapper;
    private final CommentMapper commentMapper;


    @Override
    public ItemWithBookingsDto findItem(Long id, Long userId) {
        Item item = getItem(id);
        ItemWithBookingsDto itemDto = itemMapper.itemToItemWithBookingsDto(item);
        List<CommentResponseDto> comments = commentMapper.map(commentRepository.findCommentsByItem_Id(id));
        itemDto.setComments(Set.copyOf(comments));
        if (item.getOwner().getId().equals(userId)) {
            BookingShortDto lastBooking = bookingMapper.bookingToBookingShortDto(
                    bookingRepository.findFirstByItem_IdAndEndIsBeforeOrderByEndDesc(
                            id, LocalDateTime.now()
                    )
            );
            BookingShortDto nextBooking = bookingMapper.bookingToBookingShortDto(
                    bookingRepository.findFirstByItem_IdAndStartIsAfterOrderByStartAsc(
                            id, LocalDateTime.now()
                    )
            );
            itemDto.setLastBooking(lastBooking);
            itemDto.setNextBooking(nextBooking);
        }
        return itemDto;
    }

    @Override
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
                                                        .findFirstByItem_IdAndEndIsBeforeOrderByEndDesc(
                                                                item.getId(), LocalDateTime.now()
                                                        )
                                                )
                                        );
                                        item.setNextBooking(
                                                bookingMapper.bookingToBookingShortDto(bookingRepository
                                                        .findFirstByItem_IdAndStartIsAfterOrderByStartAsc(
                                                                item.getId(), LocalDateTime.now()
                                                        )
                                                )
                                        );
                                        item.setComments(Set.copyOf(commentMapper.map(
                                                        commentRepository.findCommentsByItem_Id(item.getId())
                                                )
                                        ));
                                    })
                                    .collect(Collectors.toList())
                    )
                    .build();
        } else {
            throw new NotFoundException("No user with ID: " + ownerId);
        }
    }

    @Override
    @Transactional
    public ResponseItemDto createItem(ItemDto itemDto, Long ownerId) {
        Item newItem = itemMapper.itemDtoToItem(itemDto);
        newItem.setOwner(userRepository
                .findById(ownerId)
                .orElseThrow(() -> new NotFoundException("No user with ID: " + ownerId)));
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
                    .items(itemMapper.mapResponse(
                            itemRepository.findAllByDescriptionContainingIgnoreCaseAndAvailableIsTrue(text, pageRequest)
                    ))
                    .build();
        } else {
            return ItemListDto.builder().items(new ArrayList<>()).build();
        }
    }

    @Override
    @Transactional
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
