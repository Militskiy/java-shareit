package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.Queries;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.dto.ResponseUserDto;
import ru.practicum.shareit.user.dto.UpdateUserDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserListDto;
import ru.practicum.shareit.user.exceptions.DuplicateEmailException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@ActiveProfiles("test")
@Transactional
class UserServiceTest implements Queries {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    private UserDto userCreateDtoOne;
    private UserDto userCreateDtoTwo;


    @BeforeEach
    void setUp() {
        userCreateDtoOne = UserDto.builder().email("user1@email.com").name("User1").build();
        userCreateDtoTwo = UserDto.builder().email("user2@email.com").name("User2").build();
    }

    @Test
    void givenUserSavedInRepository_whenGettingUser_thenUserIsFound() {
        ResponseUserDto savedUser = userService.createUser(userCreateDtoOne);
        ResponseUserDto foundUser = userService.findUser(savedUser.getId());
        assertThat(savedUser).usingRecursiveComparison().isEqualTo(foundUser);
    }

    @Test
    void findAllUsers() {
        ResponseUserDto savedUserOne = userService.createUser(userCreateDtoOne);
        ResponseUserDto savedUserTwo = userService.createUser(userCreateDtoTwo);
        UserListDto result = userService.findAllUsers(PageRequest.of(0, 10));
        assertThat(result.getUserDtoList()).hasSize(2);
        assertThat(savedUserOne).usingRecursiveComparison().isEqualTo(result.getUserDtoList().get(0));
        assertThat(savedUserTwo).usingRecursiveComparison().isEqualTo(result.getUserDtoList().get(1));
    }

    @Test
    @Sql(statements = RESET_USERS_ID)
    void givenValidUserDto_whenCreatingUser_thenGetResponseDto() {
        ResponseUserDto result = userService.createUser(userCreateDtoOne);
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(userCreateDtoOne);
    }

    @Test
    void givenUserWithDuplicateEmail_whenSavingUser_thenThrowDataIntegrityViolationException() {
        userService.createUser(userCreateDtoOne);
        assertThatThrownBy(() -> userService.createUser(userCreateDtoOne))
                .isInstanceOf(DataIntegrityViolationException.class)
                .hasMessageContaining("ConstraintViolationException");
    }

    @Test
    void givenValidUpdateUserDto_whenUpdatingUser_thenUserIsUpdated() {
        ResponseUserDto createResult = userService.createUser(userCreateDtoOne);
        UpdateUserDto update = UpdateUserDto.builder().name("Update").build();
        ResponseUserDto updateResult = userService.updateUser(update, createResult.getId());
        assertThat(createResult.getName()).isNotEqualTo(updateResult.getName());
        assertThat(update.getName()).isEqualTo(updateResult.getName());
        assertThat(createResult.getEmail()).isEqualTo(updateResult.getEmail());
    }

    @Test
    void givenUserWithDuplicateEmail_whenUpdating_ThrowDuplicateEmailException() {
        userService.createUser(userCreateDtoOne);
        Long id = userService.createUser(userCreateDtoTwo).getId();
        UpdateUserDto update = UpdateUserDto.builder().email("User1@email.com").build();
        assertThatThrownBy(() -> userService.updateUser(update, id)).isInstanceOf(DuplicateEmailException.class);
    }

    @Test
    void deleteUser() {
        Long id = userService.createUser(userCreateDtoOne).getId();
        userService.deleteUser(id);
        assertThat(userRepository.findAll()).hasSize(0);
    }
}