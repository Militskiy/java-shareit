package ru.practicum.shareit.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.user.dto.ResponseUserDto;
import ru.practicum.shareit.user.dto.UpdateUserDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserListDto;
import ru.practicum.shareit.user.service.UserService;

import static ru.practicum.shareit.util.Convert.toPageRequest;

@RestController
@RequestMapping(path = "/users")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "User services")
public class UserController {

    private final UserService userService;

    @GetMapping
    @Operation(summary = "Get a list of all users")
    public ResponseEntity<UserListDto> findAllUsers(
            @RequestParam(defaultValue = "0") Integer from,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        log.info("Getting a list of all users");
        return ResponseEntity.ok(userService.findAllUsers(toPageRequest(from, size)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a specific user")
    public ResponseEntity<ResponseUserDto> findUser(@PathVariable Long id) {
        log.info("Getting a user with ID: {}", id);
        return ResponseEntity.ok(userService.findUser(id));
    }

    @PostMapping
    @Operation(summary = "Creates a new user")
    public ResponseEntity<ResponseUserDto> createUser(@RequestBody UserDto userDto) {
        log.info("Creating a new user: {}", userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(userDto));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update user")
    public ResponseEntity<ResponseUserDto> updateUser(
            @PathVariable Long id,
            @RequestBody UpdateUserDto updateUserDto
    ) {
        log.info("Updating user with ID: " + id);
        return ResponseEntity.ok(userService.updateUser(updateUserDto, id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        log.info("Deleting user with ID: " + id);
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }
}
