package ru.practicum.shareit.request.dao;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.Queries;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Set;

@DataJpaTest
class ItemRequestRepositoryTest implements Queries {
    @Autowired
    private ItemRequestRepository itemRequestRepository;
    private ItemRequest itemRequest1;
    private ItemRequest itemRequest2;

    @BeforeEach
    void setUp() {
        itemRequest1 = ItemRequest.builder()
                .requester(User.builder()
                        .id(2L)
                        .name("User2")
                        .email("User2@email.com")
                        .build())
                .description("Item request")
                .created(LocalDateTime.now())
                .build();

        itemRequest2 = ItemRequest.builder()
                .id(1L)
                .requester(User.builder()
                        .id(2L)
                        .name("User2")
                        .email("User2@email.com")
                        .build())
                .description("Item request")
                .created(LocalDateTime.now())
                .build();
    }

    @Test
    @Sql(statements = {RESET_USERS_ID, ADD_USER, ADD_USER_2})
    void findItemRequestsByRequester_Id() {
        itemRequestRepository.save(itemRequest1);
        var result = itemRequestRepository.getReferenceById(1L);
        Set<ItemRequest> testSet = Set.of(result);
        Assertions.assertThat(testSet).contains(itemRequest2);
    }
}