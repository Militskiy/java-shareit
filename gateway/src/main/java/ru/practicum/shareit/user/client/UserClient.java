package ru.practicum.shareit.user.client;


import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.practicum.shareit.BaseClient;
import ru.practicum.shareit.user.dto.UserCreateRequest;
import ru.practicum.shareit.user.dto.UserUpdateRequest;

@FeignClient(value = "users", path = "/users", url = "${shareit-server.url}")
@Component
public interface UserClient extends BaseClient<UserCreateRequest> {
    @Cacheable(cacheNames = "users", key = "#id")
    @RequestMapping(method = RequestMethod.GET, value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Object> get(@PathVariable("id") Long id);

    @CacheEvict(cacheNames = "users", key = "#id")
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Void> delete(@PathVariable("id") Long id);

    @CachePut(cacheNames = "users", key = "#id")
    @RequestMapping(method = RequestMethod.PATCH,
            value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Object> patch(
            @PathVariable("id") Long id,
            @RequestBody UserUpdateRequest updateRequest
    );
}
