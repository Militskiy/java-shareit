package ru.practicum.shareit.booking.dto;

import org.mapstruct.BeanMapping;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import ru.practicum.shareit.booking.model.Booking;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface BookingMapper {
    @Mapping(source = "itemId", target = "item.id")
    @Mapping(source = "itemName", target = "item.name")
    @Mapping(source = "bookerId", target = "booker.id")
    Booking bookingDtoToBooking(BookingDto bookingDto);

    @InheritInverseConfiguration(name = "bookingDtoToBooking")
    BookingDto bookingToBookingDto(Booking booking);

    @InheritConfiguration(name = "bookingDtoToBooking")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Booking updateBookingFromBookingDto(BookingDto bookingDto, @MappingTarget Booking booking);
}
