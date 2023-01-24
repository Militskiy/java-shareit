package ru.practicum.shareit.user.client;


import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.practicum.shareit.BaseClient;
import ru.practicum.shareit.user.dto.UserCreateRequest;
import ru.practicum.shareit.user.dto.UserResponse;
import ru.practicum.shareit.user.dto.UserUpdateRequest;

@FeignClient(value = "users", path = "/users", url = "${shareit-server.url}")
public interface UserClient extends BaseClient<UserCreateRequest> {
    @Cacheable(cacheNames = "users", key = "#id")
    @RequestMapping(
            method = RequestMethod.GET, value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    UserResponse get(@PathVariable("id") Long id);

    @CacheEvict(cacheNames = "users", key = "#id")
    @RequestMapping(
            method = RequestMethod.DELETE, value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    void delete(@PathVariable("id") Long id);

    @CachePut(cacheNames = "users", key = "#id")
    @RequestMapping(
            method = RequestMethod.PATCH,
            value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    UserResponse patch(
            @PathVariable("id") Long id,
            @RequestBody UserUpdateRequest updateRequest
    );

    @CachePut(cacheNames = "users", key = "#result.id")
    @RequestMapping(
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    UserResponse postUser(
            @RequestBody UserCreateRequest createRequest
    );
}
