package ru.practicum.shareit.user.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserListDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.exceptions.DuplicateEmailException;
import ru.practicum.shareit.user.exceptions.UserNotFoundException;
import ru.practicum.shareit.user.model.User;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.Set;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final Validator validator;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDto findUser(Long id) {
        return userMapper.userToUserDto(getUser(id));
    }

    @Override
    public UserListDto findAllUsers(PageRequest pageRequest) {
        return UserListDto.builder()
                .userDtoList(getUsers(pageRequest).map(userMapper::userToUserDto).toList()).
                build();
    }

    @Override
    @Transactional
    public UserDto createUser(UserDto userDto) {
        User newUser = userMapper.userDtoToUser(userDto);
        if (userRepository.findAll().stream().noneMatch(user -> user.getEmail().equals(newUser.getEmail()))) {
            return userMapper.userToUserDto(userRepository.save(newUser));
        } else {
            throw new DuplicateEmailException("Email: " + newUser.getEmail() + " already exists");
        }
    }

    @Override
    @Transactional
    public UserDto updateUser(UserDto partialUserDto, Long userId) {
        User targetUser = getUser(userId);
        User updatedUser = targetUser.toBuilder().build();
        userMapper.updateUserFromDto(partialUserDto, updatedUser);
        Set<ConstraintViolation<User>> violations = validator.validate(updatedUser);
        if (violations.isEmpty()) {
            if (userRepository.findAll()
                    .stream()
                    .filter(user -> !user.getId().equals(updatedUser.getId()))
                    .noneMatch(user -> user.getEmail().equals(updatedUser.getEmail()))) {
                return userMapper.userToUserDto(userRepository.save(updatedUser));
            } else {
                throw new DuplicateEmailException("Email: " + updatedUser.getEmail() + " already exists");
            }
        } else {
            throw new ConstraintViolationException(violations);
        }
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    private User getUser(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("No user with ID: " + id));
    }

    private Page<User> getUsers(PageRequest pageRequest) {
        return userRepository.findAll(pageRequest);
    }
}
