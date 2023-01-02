package ru.practicum.shareit.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Convert {
    public static PageRequest toPageRequest(int from, int size) {
        return PageRequest.of((from / size), size);
    }
}
