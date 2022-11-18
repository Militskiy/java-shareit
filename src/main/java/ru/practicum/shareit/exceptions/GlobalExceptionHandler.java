package ru.practicum.shareit.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.item.exceptions.ItemNotFoundException;
import ru.practicum.shareit.item.exceptions.WrongUserException;
import ru.practicum.shareit.user.exceptions.DuplicateEmailException;
import ru.practicum.shareit.user.exceptions.UserNotFoundException;

import java.util.Objects;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<String> handleDuplicateEmailException(final DuplicateEmailException e) {
        log.info(e.getMessage());
        return ResponseEntity.status(409).body(e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<String> handleUserNotFoundException(final UserNotFoundException e) {
        log.info(e.getMessage());
        return ResponseEntity.status(404).body(e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<String> handleItemNotFoundException(final ItemNotFoundException e) {
        log.info(e.getMessage());
        return ResponseEntity.status(404).body(e.getMessage());
    }
    @ExceptionHandler
    public ResponseEntity<String> handleWrongUserException(final WrongUserException e) {
        log.info(e.getMessage());
        return ResponseEntity.status(403).body(e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<String> handleThrowable(final Throwable e) {
        log.info(ExceptionUtils.getStackTrace(e));
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Unexpected error: " + e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<String> handleMissingRequestHeaderException(final MissingRequestHeaderException e) {
        log.info(e.getMessage());
        return ResponseEntity.status(400).body(e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<String> handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        log.info(String.format("Validation failed - field: %s, message: %s",
                Objects.requireNonNull(e.getBindingResult().getFieldError()).getField(),
                Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage()));
        return ResponseEntity.status(400).body(String.format("Validation failed - field: %s, message: %s",
                Objects.requireNonNull(e.getBindingResult().getFieldError()).getField(),
                Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage()));
    }
}
