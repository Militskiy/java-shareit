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
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/users")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "User services")
public class UserController {

    private final UserService userService;

    @GetMapping
    @Operation(summary = "Get a list of all users")
    public ResponseEntity<List<UserDto>> findAllUsers() {
        log.info("Getting a list of all users");
        return ResponseEntity.ok(userService.findAllUsers()
                .stream()
                .map(UserMapper.INSTANCE::userToUserDto)
                .collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a specific user")
    public ResponseEntity<UserDto> findUser(@PathVariable @Min(1) Long id) {
        log.info("Getting a user with ID: {}", id);
        return ResponseEntity.ok(UserMapper.INSTANCE.userToUserDto(userService.findUser(id)));
    }

    @PostMapping
    @Operation(summary = "Creates a new user")
    public ResponseEntity<UserDto> createUser(@RequestBody @Valid UserDto userDto) {
        log.info("Creating a new user: {}", userDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        UserMapper.INSTANCE.userToUserDto(
                                userService.createUser(UserMapper.INSTANCE.userDtoToUser(userDto))
                        )
                );
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update user")
    public ResponseEntity<UserDto> updateUser(
            @PathVariable @Min(1) Long id,
            @RequestBody UserDto userDto
    ) {
        log.info("Updating user with ID: " + id);
        User user = userService.findUser(id);
        User updatedUser = User.builder().id(user.getId()).name(user.getName()).email(user.getEmail()).build();
        UserMapper.INSTANCE.updateUserFromDto(userDto, updatedUser);
        return ResponseEntity.ok(UserMapper.INSTANCE.userToUserDto(userService.updateUser(updatedUser)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user")
    public void deleteUser(@PathVariable @Min(1) Long id) {
        log.info("Deleting user with ID: " + id);
        userService.deleteUser(id);
    }
}
