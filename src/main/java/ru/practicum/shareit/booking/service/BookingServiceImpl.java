package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingListDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.exceptions.BookingApprovalException;
import ru.practicum.shareit.booking.exceptions.NotAvailableException;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.exceptions.ItemNotFoundException;
import ru.practicum.shareit.item.exceptions.WrongUserException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.exceptions.UserNotFoundException;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingMapper mapper;

    @Override
    public BookingResponseDto findBooking(Long id, Long userId) {
        return mapper.bookingToBookingResponseDto(
                bookingRepository.findBooking(id, userId).orElseThrow(() -> new NotFoundException("Booking not found"))
        );
    }

    @Override
    public BookingListDto findBookingsByBookerAndState(Long bookerId, State state, PageRequest pageRequest) {
        if (userRepository.existsById(bookerId)) {
            switch (state) {
                case ALL:
                    return BookingListDto.builder()
                            .bookings(mapper.map(
                                    bookingRepository.findBookingsByBookerIdOrderByStartDesc(bookerId, pageRequest)
                            ))
                            .build();
                case CURRENT:
                    return BookingListDto.builder().bookings(mapper.map(
                            bookingRepository.findBookingsByBookerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(
                                    bookerId, LocalDateTime.now(), LocalDateTime.now(), pageRequest
                            ))).build();
                case PAST:
                    return BookingListDto.builder()
                            .bookings(mapper.map(
                                    bookingRepository.findBookingsByBookerIdAndEndIsBeforeOrderByStartDesc(
                                            bookerId, LocalDateTime.now(), pageRequest
                                    )
                            ))
                            .build();
                case FUTURE:
                    return BookingListDto.builder()
                            .bookings(mapper.map(
                                    bookingRepository.findBookingsByBookerIdAndStartIsAfterOrderByStartDesc(
                                            bookerId, LocalDateTime.now(), pageRequest
                                    )
                            ))
                            .build();
                case WAITING:
                    return BookingListDto.builder()
                            .bookings(mapper.map(
                                    bookingRepository.findBookingsByBookerIdAndStatusIs(
                                            bookerId, Status.WAITING, pageRequest
                                    )
                            ))
                            .build();
                case REJECTED:
                    return BookingListDto.builder()
                            .bookings(mapper.map(
                                    bookingRepository.findBookingsByBookerIdAndStatusIs(
                                            bookerId, Status.REJECTED, pageRequest
                                    )
                            ))
                            .build();
                default:
                    throw new IllegalArgumentException("Unknown state: UNSUPPORTED_STATUS");
            }
        } else {
            throw new NotFoundException("No user with ID: " + bookerId);
        }
    }

    @Override
    public BookingListDto findBookingsByOwnerAndState(Long ownerId, State state, PageRequest pageRequest) {
        if (userRepository.existsById(ownerId)) {
            switch (state) {
                case ALL:
                    return BookingListDto.builder()
                            .bookings(mapper.map(
                                    bookingRepository.findBookingsByItemOwnerIdOrderByStartDesc(ownerId, pageRequest)
                            ))
                            .build();
                case CURRENT:
                    return BookingListDto.builder().bookings(mapper.map(
                            bookingRepository.findBookingsByItemOwnerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(
                                    ownerId, LocalDateTime.now(), LocalDateTime.now(), pageRequest
                            ))).build();
                case PAST:
                    return BookingListDto.builder()
                            .bookings(mapper.map(
                                    bookingRepository.findBookingsByItemOwnerIdAndEndIsBeforeOrderByStartDesc(
                                            ownerId, LocalDateTime.now(), pageRequest
                                    )
                            ))
                            .build();
                case FUTURE:
                    return BookingListDto.builder()
                            .bookings(mapper.map(
                                    bookingRepository.findBookingsByItemOwnerIdAndStartIsAfterOrderByStartDesc(
                                            ownerId, LocalDateTime.now(), pageRequest
                                    )
                            ))
                            .build();
                case WAITING:
                    return BookingListDto.builder()
                            .bookings(mapper.map(
                                    bookingRepository.findBookingsByItemOwnerIdAndStatusIs(
                                            ownerId, Status.WAITING, pageRequest
                                    )
                            ))
                            .build();
                case REJECTED:
                    return BookingListDto.builder()
                            .bookings(mapper.map(
                                    bookingRepository.findBookingsByItemOwnerIdAndStatusIs(
                                            ownerId, Status.REJECTED, pageRequest
                                    )
                            ))
                            .build();
                default:
                    throw new IllegalArgumentException("Unknown state: UNSUPPORTED_STATUS");
            }
        } else {
            throw new NotFoundException("No user with ID: " + ownerId);
        }
    }

    @Override
    @Transactional
    public BookingResponseDto createBooking(BookingCreateDto bookingCreateDto, Long bookerId) {
        Item item = itemRepository
                .findById(bookingCreateDto.getItemId())
                .orElseThrow(() -> new ItemNotFoundException("No such item with ID: " + bookingCreateDto.getItemId()));
        if (!item.getOwner().getId().equals(bookerId)) {
            if (item.getAvailable()) {
                User booker = userRepository
                        .findById(bookerId)
                        .orElseThrow(() -> new UserNotFoundException("No user with ID: " + bookerId));
                Booking newBooking = mapper.bookingCreateDtoToBooking(bookingCreateDto);
                newBooking.setBooker(booker);
                newBooking.setItem(item);
                newBooking.setStatus(Status.WAITING);
                return mapper.bookingToBookingResponseDto(bookingRepository.save(newBooking));
            } else {
                throw new NotAvailableException("Item is not available for booking");
            }
        } else {
            throw new NotFoundException("Owner cannot book own item");
        }
    }

    @Override
    @Transactional
    public BookingResponseDto confirmBooking(Long id, Long userId, Boolean approved) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("No booking with such ID: " + id));
        if (booking.getItem().getOwner().getId().equals(userId)) {
            if (booking.getStatus().equals(Status.WAITING)) {
                if (approved) {
                    booking.setStatus(Status.APPROVED);
                } else {
                    booking.setStatus(Status.REJECTED);
                }
                return mapper.bookingToBookingResponseDto(bookingRepository.save(booking));
            } else {
                throw new BookingApprovalException("Booking is already approved / rejected");
            }
        } else {
            throw new WrongUserException("Can only approve own bookings");
        }
    }
}
