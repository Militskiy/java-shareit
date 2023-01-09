package ru.practicum.shareit.request.client;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.shareit.BaseClient;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;

@FeignClient(value = "requests", path = "/requests", url = "${shareit-server.url}")
public interface ItemRequestClient extends BaseClient<ItemRequestCreateDto> {
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/all",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<Object> getAllRequests(
            @RequestHeader(HEADER_USER_ID) Long userId,
            @RequestParam Integer from,
            @RequestParam Integer size
    );

    @Cacheable(cacheNames = "requests", key = "#requestId")
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/{requestId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<Object> get(
            @RequestHeader(HEADER_USER_ID) Long userId,
            @PathVariable Long requestId
    );
}
