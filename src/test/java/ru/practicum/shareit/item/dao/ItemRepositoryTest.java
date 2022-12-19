package ru.practicum.shareit.item.dao;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.Queries;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.Set;

@DataJpaTest
class ItemRepositoryTest implements Queries {
    @Autowired
    private ItemRepository itemRepository;
    private Item item;

    @BeforeEach
    void setUp() {
        item = Item.builder()
                .id(1L)
                .name("Item1")
                .description("Test item1")
                .available(Boolean.TRUE)
                .owner(User.builder().id(1L).name("User1").email("User1@email.com").build())
                .build();
    }

    @Test
    @Sql(statements = {RESET_USERS_ID, RESET_ITEMS_ID, ADD_USER, ADD_ITEM})
    void itemEntityTest() {
        var result = itemRepository.getReferenceById(1L);
        Set<Item> testSet = Set.of(result);
        Assertions.assertThat(testSet).contains(item);
    }
}