package ru.practicum.shareit.user.dao;

import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    Optional<User> findUser(Long id);
    List<User> findAllUsers();
    User createUser(User user);
    User updateUser(User user);
    void deleteUser(Long id);
}
