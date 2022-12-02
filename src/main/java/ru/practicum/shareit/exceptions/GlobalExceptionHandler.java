package ru.practicum.shareit.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.practicum.shareit.booking.exceptions.BookingApprovalException;
import ru.practicum.shareit.booking.exceptions.NotAvailableException;
import ru.practicum.shareit.item.exceptions.ItemNotFoundException;
import ru.practicum.shareit.item.exceptions.WrongUserException;
import ru.practicum.shareit.user.exceptions.DuplicateEmailException;
import ru.practicum.shareit.user.exceptions.UserNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<String> handleDuplicateEmailException(final DuplicateEmailException e) {
        log.warn(e.getMessage());
        return ResponseEntity.status(409).body(e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<String> handleNotFoundException(final NotFoundException e) {
        log.warn(e.getMessage());
        return ResponseEntity.status(404).body(e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<String> handleUserNotFoundException(final UserNotFoundException e) {
        log.warn(e.getMessage());
        return ResponseEntity.status(404).body(e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<String> handleNotAvailableException(final NotAvailableException e) {
        log.warn(e.getMessage());
        return ResponseEntity.status(400).body(e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<String> handleItemNotFoundException(final ItemNotFoundException e) {
        log.warn(e.getMessage());
        return ResponseEntity.status(404).body(e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<String> handleWrongUserException(final WrongUserException e) {
        log.warn(e.getMessage());
        return ResponseEntity.status(404).body(e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleBookingApprovalException(final BookingApprovalException e) {
        log.warn(e.getMessage());
        return ResponseEntity.status(400).body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<String> handleThrowable(final Throwable e) {
        log.error(ExceptionUtils.getStackTrace(e));
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Unexpected error: " + e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<String> handleMissingRequestHeaderException(final MissingRequestHeaderException e) {
        log.warn(e.getMessage());
        return ResponseEntity.status(400).body(e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(
            final MethodArgumentTypeMismatchException e
    ) {
        String error = "Unknown " + e.getName() + ": " + e.getValue();
        log.warn(error);
        return ResponseEntity.status(400).body(new ErrorResponse(error));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleMissingServletRequestParameterException(
            final MissingServletRequestParameterException e
    ) {
        String error = "Missing request parameter: " + e.getParameterName();
        log.warn(error);
        return ResponseEntity.status(400).body(new ErrorResponse(error));
    }

    @ExceptionHandler(value = {DataIntegrityViolationException.class})
    public ResponseEntity<String> handleConstraintViolationException(DataIntegrityViolationException e) {
        log.warn(e.getMessage());
        return ResponseEntity.status(409).body(e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<String> handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        List<String> errorList = e.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
        String errors = errorList.toString().substring(1, errorList.toString().length() - 1);
        log.warn(errors);
        return ResponseEntity.status(400).body(errors);
    }
}
