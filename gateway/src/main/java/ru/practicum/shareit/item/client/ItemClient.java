package ru.practicum.shareit.item.client;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.shareit.BaseClient;
import ru.practicum.shareit.item.dto.CommentCreateRequest;
import ru.practicum.shareit.item.dto.ItemCreateRequest;
import ru.practicum.shareit.item.dto.ItemUpdateRequest;

@FeignClient(value = "items", path = "/items", url = "${shareit-server.url}")
public interface ItemClient extends BaseClient<ItemCreateRequest> {
    @Cacheable(cacheNames = "items", key = "{#id, #userId}")
    @RequestMapping(method = RequestMethod.GET, value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Object> get(
            @RequestHeader(HEADER_USER_ID) Long userId,
            @PathVariable("id") Long id
    );

    @CacheEvict(cacheNames = "items", allEntries = true)
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Void> delete(
            @RequestHeader(HEADER_USER_ID) Long userId,
            @PathVariable("id") Long id
    );

    @CacheEvict(cacheNames = "items", allEntries = true)
    @RequestMapping(method = RequestMethod.PATCH, value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Object> patch(
            @RequestBody ItemUpdateRequest updateRequest,
            @RequestHeader(HEADER_USER_ID) Long userId,
            @PathVariable("id") Long id
    );

    @RequestMapping(method = RequestMethod.GET, value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Object> search(
            @RequestParam String text,
            @RequestParam Integer from,
            @RequestParam Integer size
    );

    @CacheEvict(cacheNames = "items", allEntries = true)
    @RequestMapping(method = RequestMethod.POST, value = "/{id}/comment", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Object> comment(
            @RequestHeader(HEADER_USER_ID) Long userId,
            @PathVariable("id") Long id,
            @RequestBody CommentCreateRequest comment
    );
}
