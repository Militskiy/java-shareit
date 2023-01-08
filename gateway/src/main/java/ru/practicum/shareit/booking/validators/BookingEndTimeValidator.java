package ru.practicum.shareit.booking.validators;

import ru.practicum.shareit.booking.annotations.EndAfterStart;
import ru.practicum.shareit.booking.dto.BookingCreateRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class BookingEndTimeValidator implements ConstraintValidator<EndAfterStart, BookingCreateRequest> {
    @Override
    public boolean isValid(BookingCreateRequest booking, ConstraintValidatorContext context) {
        if (booking.getStart() != null && booking.getEnd() != null) {
            return booking.getEnd().isAfter(booking.getStart());
        }
        return false;
    }
}
