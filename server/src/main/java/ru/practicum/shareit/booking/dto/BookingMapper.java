package ru.practicum.shareit.booking.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.user.dto.UserMapper;

import java.util.List;

@Mapper(
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = "spring",
        uses = {ItemMapper.class, UserMapper.class}
)
public interface BookingMapper {

    BookingResponseDto bookingToBookingResponseDto(Booking booking);

    @Mapping(source = "itemId", target = "item.id")
    Booking bookingCreateDtoToBooking(BookingCreateDto bookingCreateDto);

    List<BookingResponseDto> map(Page<Booking> bookings);

    @Mapping(source = "booker.id", target = "bookerId")
    BookingShortDto bookingToBookingShortDto(Booking booking);
}
