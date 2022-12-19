package ru.practicum.shareit.item.dao;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.Queries;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Set;

@DataJpaTest
class CommentRepositoryTest implements Queries {
    private static final String TEST = "insert into comments (created, text, author_id, item_id) " +
            "values ('2022-10-19 00:00:00.000000', 'comment', 2, 1)";
    @Autowired
    private CommentRepository commentRepository;
    private Comment comment;

    @BeforeEach
    void setUp() {
        comment = Comment.builder()
                .id(1L)
                .created(LocalDateTime.of(2022, 10, 19, 0, 0, 0))
                .item(Item.builder()
                        .id(1L)
                        .name("Item1")
                        .description("Test item1")
                        .available(Boolean.TRUE)
                        .owner(User.builder().id(1L).name("User1").email("User1@email.com").build())
                        .build())
                .author(User.builder().id(2L).name("User2").email("User2@email.com").build())
                .text("comment")
                .build();
    }

    @Test
    @Sql(statements = {RESET_USERS_ID, RESET_ITEMS_ID, ADD_USER, ADD_ITEM, ADD_USER_2, ADD_BOOKING_ITEM1_USER2, TEST})
    void commentEntityTest() {
        var result = commentRepository.getReferenceById(1L);
        Set<Comment> testSet = Set.of(result);
        Assertions.assertThat(testSet).contains(comment);
    }
}