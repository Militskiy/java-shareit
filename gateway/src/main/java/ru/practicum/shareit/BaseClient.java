package ru.practicum.shareit;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

public interface BaseClient<T> {
    String HEADER_USER_ID = "X-Sharer-User-Id";

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Object> get(
            @RequestParam Integer from,
            @RequestParam Integer size
    );

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Object> get(
            @RequestHeader(HEADER_USER_ID) Long userId,
            @RequestParam Integer from,
            @RequestParam Integer size
    );

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Object> post(@RequestBody T createRequest);

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Object> post(
            @RequestBody T createRequest,
            @RequestHeader(HEADER_USER_ID) Long userId
    );
}
