package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {
    User findUser(Long id);

    List<User> findAllUsers();

    User createUser(User user);

    User updateUser(User user);

    void deleteUser(Long id);
}
