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
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional(readOnly = true)
    public ResponseUserDto findUser(Long id) {
        return userMapper.userToResponseUserDto(getUser(id));
    }

    @Override
    @Transactional(readOnly = true)
    public UserListDto findAllUsers(PageRequest pageRequest) {
        return UserListDto
                .builder()
                .userDtoList(userMapper.map(userRepository.findAll(pageRequest)))
                .build();
    }

    @Override
    public ResponseUserDto createUser(UserDto userDto) {
        return userMapper.userToResponseUserDto(userRepository.save(userMapper.userDtoToUser(userDto)));
    }

    @Override
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
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    private User getUser(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("No user with ID: " + id));
    }
}
