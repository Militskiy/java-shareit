package ru.practicum.shareit.user.service;

import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.user.dto.UpdateUserDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserListDto;

public interface UserService {
    UserDto findUser(Long id);

    UserListDto findAllUsers(PageRequest pageRequest);

    UserDto createUser(UserDto userDto);

    UserDto updateUser(UpdateUserDto updateUserDto, Long id);

    void deleteUser(Long id);
}
