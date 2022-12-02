package ru.practicum.shareit.booking.annotations;

import ru.practicum.shareit.booking.validators.BookingEndTimeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = BookingEndTimeValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface EndAfterStart {
    String message() default "Booking end time cannot be before start time";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
