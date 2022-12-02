package ru.practicum.shareit.booking.exceptions;

public class NotAvailableException extends RuntimeException {
    public NotAvailableException(String message) {
        super(message);
    }
}
