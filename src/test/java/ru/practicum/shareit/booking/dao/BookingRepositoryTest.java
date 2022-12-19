package ru.practicum.shareit.booking.dao;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.Queries;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Set;

@DataJpaTest
class BookingRepositoryTest implements Queries {

    @Autowired
    private BookingRepository bookingRepository;
    private Booking booking1;
    private Booking booking2;

    @BeforeEach
    void setUp() {
        booking1 = Booking.builder()
                .start(LocalDateTime.now().plusHours(1))
                .end(LocalDateTime.now().plusHours(2))
                .status(Status.WAITING)
                .item(Item.builder()
                        .id(1L)
                        .name("Item1")
                        .description("Test item1")
                        .available(Boolean.TRUE)
                        .owner(User.builder().id(1L).name("User1").email("User1@email.com").build())
                        .build())
                .booker(User.builder().id(2L).name("User2").email("User2@email.com").build())
                .build();
        booking2 = Booking.builder()
                .id(1L)
                .start(LocalDateTime.now().plusHours(1))
                .end(LocalDateTime.now().plusHours(2))
                .status(Status.WAITING)
                .item(Item.builder()
                        .id(1L)
                        .name("Item1")
                        .description("Test item1")
                        .available(Boolean.TRUE)
                        .owner(User.builder().id(1L).name("User1").email("User1@email.com").build())
                        .build())
                .booker(User.builder().id(2L).name("User2").email("User2@email.com").build())
                .build();
    }

    @Test
    @Sql(statements = {RESET_IDS, ADD_USER, ADD_USER_2, ADD_ITEM})
    void bookingEntityTest() {
        var createResult = bookingRepository.save(booking1);
        Set<Booking> resultSet = Set.of(createResult);
        Assertions.assertThat(resultSet).contains(booking2);
    }
}