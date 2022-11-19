package ru.practicum.shareit.user.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dao.UserDao;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.exceptions.DuplicateEmailException;
import ru.practicum.shareit.user.exceptions.UserNotFoundException;
import ru.practicum.shareit.user.model.User;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserDao userRepository;
    private final Validator validator;

    @Override
    public UserDto findUser(Long id) {
        return UserMapper.INSTANCE.userToUserDto(getUser(id));
    }

    @Override
    public List<UserDto> findAllUsers() {
        return userRepository.findAllUsers()
                .stream()
                .map(UserMapper.INSTANCE::userToUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        User newUser = UserMapper.INSTANCE.userDtoToUser(userDto);
        if (findAllUsers().stream().noneMatch(user -> user.getEmail().equals(newUser.getEmail()))) {
            return UserMapper.INSTANCE.userToUserDto(userRepository.createUser(newUser));
        } else {
            throw new DuplicateEmailException("Email: " + newUser.getEmail() + " already exists");
        }
    }

    @Override
    public UserDto updateUser(UserDto partialUserDto, Long userId) {
        User targetUser = getUser(userId);
        User updatedUser = targetUser.toBuilder().build();
        UserMapper.INSTANCE.updateUserFromDto(partialUserDto, updatedUser);
        Set<ConstraintViolation<User>> violations = validator.validate(updatedUser);
        if (violations.isEmpty()) {
            if (findAllUsers()
                    .stream()
                    .filter(user -> !user.getId().equals(updatedUser.getId()))
                    .noneMatch(user -> user.getEmail().equals(updatedUser.getEmail()))) {
                return UserMapper.INSTANCE.userToUserDto(userRepository.updateUser(updatedUser));
            } else {
                throw new DuplicateEmailException("Email: " + updatedUser.getEmail() + " already exists");
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

    private User getUser(Long id) {
        return userRepository.findUser(id).orElseThrow(() -> new UserNotFoundException("No user with ID: " + id));
    }
}
