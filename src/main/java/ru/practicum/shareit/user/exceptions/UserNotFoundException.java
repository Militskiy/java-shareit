package ru.practicum.shareit.user.exceptions;

import java.util.NoSuchElementException;

public class UserNotFoundException extends NoSuchElementException {
    public UserNotFoundException(String s) {
        super(s);
    }
}
