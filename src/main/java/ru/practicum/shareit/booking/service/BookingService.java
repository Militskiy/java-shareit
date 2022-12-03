package ru.practicum.shareit.booking.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingListDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.State;

@Service
public interface BookingService {

    BookingResponseDto findBooking(Long id, Long userId);

    BookingResponseDto createBooking(BookingCreateDto bookingCreateDto, Long bookerId);

    BookingResponseDto confirmBooking(Long id, Long userId, Boolean approved);

    BookingListDto findBookingsByBookerAndState(Long userId, State state, PageRequest pageRequest);

    BookingListDto findBookingsByOwnerAndState(Long userId, State state, PageRequest pageRequest);
}
