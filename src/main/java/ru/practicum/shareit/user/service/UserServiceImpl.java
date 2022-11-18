package ru.practicum.shareit.user.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dao.UserDao;
import ru.practicum.shareit.user.exceptions.DuplicateEmailException;
import ru.practicum.shareit.user.exceptions.UserNotFoundException;
import ru.practicum.shareit.user.model.User;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserDao userRepository;
    private final Validator validator;

    @Override
    public User findUser(Long id) {
        return userRepository.findUser(id).orElseThrow(() -> new UserNotFoundException("No user with ID: " + id));
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAllUsers();
    }

    @Override
    public User createUser(User user) {
        if (findAllUsers().stream().anyMatch(user1 -> user1.getEmail().equals(user.getEmail()))) {
            throw new DuplicateEmailException("Email: " + user.getEmail() + " already exists");
        } else {
            return userRepository.createUser(user);
        }
    }

    @Override
    public User updateUser(User user) {
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        if (violations.isEmpty()) {
            if (findAllUsers()
                    .stream()
                    .filter(user1 -> !user1.getId().equals(user.getId()))
                    .noneMatch(user1 -> user1.getEmail().equals(user.getEmail()))) {
                return userRepository.updateUser(user);
            } else {
                throw new DuplicateEmailException("Email: " + user.getEmail() + " already exists");
            }
        } else {
            throw new ConstraintViolationException(violations);
        }
    }

    @Override
    public void deleteUser(Long id) {
        findUser(id);
        userRepository.deleteUser(id);
    }
}
