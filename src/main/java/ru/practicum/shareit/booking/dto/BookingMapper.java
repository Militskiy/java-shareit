package ru.practicum.shareit.booking.dto;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface BookingMapper {
    Booking bookingResponseDtoToBooking(BookingResponseDto bookingResponseDto);

    BookingResponseDto bookingToBookingResponseDto(Booking booking);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Booking updateBookingFromBookingResponseDto(BookingResponseDto bookingResponseDto, @MappingTarget Booking booking);

    @Mapping(source = "itemId", target = "item.id")
    Booking bookingCreateDtoToBooking(BookingCreateDto bookingCreateDto);

    @Mapping(source = "item.id", target = "itemId")
    BookingCreateDto bookingToBookingCreateDto(Booking booking);

    @Mapping(source = "itemId", target = "item.id")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Booking updateBookingFromBookingCreateDto(BookingCreateDto bookingCreateDto, @MappingTarget Booking booking);

    List<BookingResponseDto> map(Page<Booking> bookings);

    @Mapping(source = "bookerId", target = "booker.id")
    Booking bookingShortDtoToBooking(BookingShortDto bookingShortDto);

    @Mapping(source = "booker.id", target = "bookerId")
    BookingShortDto bookingToBookingShortDto(Booking booking);

    @Mapping(source = "bookerId", target = "booker.id")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Booking updateBookingFromBookingShortDto(BookingShortDto bookingShortDto, @MappingTarget Booking booking);
}
