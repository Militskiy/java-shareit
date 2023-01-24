package ru.practicum.shareit.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.user.client.UserClient;
import ru.practicum.shareit.user.dto.UserCreateRequest;
import ru.practicum.shareit.user.dto.UserResponse;
import ru.practicum.shareit.user.dto.UserUpdateRequest;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping(path = "/users")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "User services")
@Validated
public class UserController {
    private final UserClient client;

    @GetMapping
    @Operation(summary = "Get a list of all users")
    public ResponseEntity<Object> findAllUsers(
            @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(defaultValue = "10") @Positive Integer size
    ) {
        log.info("Getting a list of all users");
        return client.get(from, size);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a specific user")
    public ResponseEntity<UserResponse> findUser(@PathVariable @Positive Long id) {
        log.info("Getting a user with ID: {}", id);
        return ResponseEntity.ok(client.get(id));
    }

    @PostMapping
    @Operation(summary = "Creates a new user")
    public ResponseEntity<Object> createUser(@RequestBody @Valid UserCreateRequest userCreateRequest) {
        log.info("Creating a new user: {}", userCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(client.postUser(userCreateRequest));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update user")
    public ResponseEntity<Object> updateUser(
            @PathVariable @Positive Long id,
            @RequestBody @Valid UserUpdateRequest userUpdateRequest
    ) {
        log.info("Updating user with ID: " + id);
        return ResponseEntity.ok(client.patch(id, userUpdateRequest));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user")
    public ResponseEntity<Void> deleteUser(@PathVariable @Positive Long id) {
        log.info("Deleting user with ID: " + id);
        client.delete(id);
        return ResponseEntity.ok().build();
    }
}
