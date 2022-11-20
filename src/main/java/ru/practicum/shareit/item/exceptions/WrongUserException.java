package ru.practicum.shareit.item.exceptions;

public class WrongUserException extends RuntimeException {
    public WrongUserException(String s) {
        super(s);
    }
}
