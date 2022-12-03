package ru.practicum.shareit.user.service;

import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.user.dto.ResponseUserDto;
import ru.practicum.shareit.user.dto.UpdateUserDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserListDto;

public interface UserService {
    ResponseUserDto findUser(Long id);

    UserListDto findAllUsers(PageRequest pageRequest);

    ResponseUserDto createUser(UserDto userDto);

    ResponseUserDto updateUser(UpdateUserDto updateUserDto, Long id);

    void deleteUser(Long id);
}
