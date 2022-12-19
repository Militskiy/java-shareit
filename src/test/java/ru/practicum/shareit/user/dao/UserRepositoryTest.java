package ru.practicum.shareit.user.dao;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.Queries;
import ru.practicum.shareit.user.model.User;

import java.util.Set;

@DataJpaTest
class UserRepositoryTest implements Queries {
    @Autowired
    private UserRepository userRepository;
    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .name("User1")
                .email("User1@email.com")
                .build();
    }

    @Test
    @Sql(statements = {RESET_USERS_ID, ADD_USER})
    void userEntityTest() {
        User result = userRepository.getReferenceById(1L);
        Set<User> testSet = Set.of(result);
        Assertions.assertThat(testSet).contains(user);
    }
}