package ru.practicum.shareit.item.exceptions;

import java.util.NoSuchElementException;

public class ItemNotFoundException extends NoSuchElementException {
    public ItemNotFoundException(String s) {
        super(s);
    }
}
