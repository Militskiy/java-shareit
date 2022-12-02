package ru.practicum.shareit.user.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.dto.ResponseUserDto;
import ru.practicum.shareit.user.dto.UpdateUserDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserListDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.exceptions.DuplicateEmailException;
import ru.practicum.shareit.user.model.User;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public ResponseUserDto findUser(Long id) {
        return userMapper.userToResponseUserDto(getUser(id));
    }

    @Override
    public UserListDto findAllUsers(PageRequest pageRequest) {
        return UserListDto
                .builder()
                .userDtoList(userMapper.map(userRepository.findAll(pageRequest)))
                .build();
    }

    @Override
    @Transactional
    public ResponseUserDto createUser(UserDto userDto) {
        return userMapper.userToResponseUserDto(userRepository.save(userMapper.userDtoToUser(userDto)));

    }

    @Override
    @Transactional
    public ResponseUserDto updateUser(UpdateUserDto updateUserDto, Long userId) {
        if (userRepository.findByEmailAndIdIsNot(updateUserDto.getEmail(), userId).isEmpty()) {
            User targetUser = getUser(userId);
            return userMapper.userToResponseUserDto(
                    userRepository.save(userMapper.updateUserFromUpdateUserDto(updateUserDto, targetUser))
            );
        } else {
            throw new DuplicateEmailException("Email: " + updateUserDto.getEmail() + " already exists");
        }
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    private User getUser(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("No user with ID: " + id));
    }
}
