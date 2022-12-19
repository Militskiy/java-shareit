package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.Queries;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.exceptions.BookingApprovalException;
import ru.practicum.shareit.booking.exceptions.NotAvailableException;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.exceptions.WrongUserException;

import java.time.LocalDateTime;
import java.time.Month;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@ActiveProfiles("test")
@Transactional
class BookingServiceTest implements Queries {
    @Autowired
    private BookingService bookingService;
    private BookingCreateDto bookingCreateDto;


    @BeforeEach
    void setUp() {
        bookingCreateDto = BookingCreateDto.builder()
                .start(LocalDateTime.now().plusHours(1))
                .end(LocalDateTime.now().plusHours(2))
                .itemId(1L)
                .build();

    }

    @Test
    @Sql(statements = {
            RESET_USERS_ID,
            RESET_ITEMS_ID,
            RESET_BOOKINGS_ID,
            ADD_USER,
            ADD_USER_2,
            ADD_ITEM,
            PAST_BOOKING
    })
    void givenBookerId_whenFindingBooking_resultIsFound() {
        var result = bookingService.findBooking(1L, 2L);
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getBooker().getId()).isEqualTo(2L);
        assertThat(result.getItem().getName()).isEqualTo("Item1");
        assertThat(result.getStatus()).isEqualTo(Status.APPROVED);
        assertThat(result.getStart().getMonth()).isEqualTo(Month.SEPTEMBER);
        assertThat(result.getEnd().getMonth()).isEqualTo(Month.OCTOBER);
    }

    @Test
    @Sql(statements = {
            RESET_USERS_ID,
            RESET_ITEMS_ID,
            RESET_BOOKINGS_ID,
            ADD_USER,
            ADD_USER_2,
            ADD_ITEM,
    })
    void givenNoBooking_whenFindingBooking_ThrowException() {
        assertThatThrownBy(() -> bookingService.findBooking(1L, 2L))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    @Sql(statements = {
            RESET_USERS_ID,
            RESET_ITEMS_ID,
            RESET_BOOKINGS_ID,
            ADD_USER,
            ADD_USER_2,
            ADD_ITEM
    })
    void givenValidItemCreateDto_whenCreatingItem_getEqualResultBack() {
        var result = bookingService.createBooking(bookingCreateDto, 2L);
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getBooker().getId()).isEqualTo(2L);
        assertThat(result.getItem().getName()).isEqualTo("Item1");
        assertThat(result.getStatus()).isEqualTo(Status.WAITING);
        assertThat(result.getStart().getHour()).isEqualTo(LocalDateTime.now().plusHours(1).getHour());
        assertThat(result.getEnd().getHour()).isEqualTo(LocalDateTime.now().plusHours(2).getHour());
    }

    @Test
    @Sql(statements = {
            RESET_USERS_ID,
            RESET_ITEMS_ID,
            RESET_BOOKINGS_ID,
            ADD_USER,
            ADD_USER_2,
            ADD_ITEM
    })
    void givenEmptyCreateDto_whenCreatingItem_getEqualResultBack() {
        assertThatThrownBy(() -> bookingService.createBooking(BookingCreateDto.builder().build(), 2L))
                .isInstanceOf(InvalidDataAccessApiUsageException.class);
    }

    @Test
    @Sql(statements = {
            RESET_USERS_ID,
            RESET_BOOKINGS_ID,
            ADD_USER,
            ADD_USER_2
    })
    void givenNoItem_whenCreatingBooking_thenThrowException() {
        assertThatThrownBy(() -> bookingService.createBooking(bookingCreateDto, 2L))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    @Sql(statements = {
            RESET_USERS_ID,
            RESET_ITEMS_ID,
            RESET_BOOKINGS_ID,
            ADD_USER,
            ADD_USER_2,
            ADD_ITEM
    })
    void givenNoBooker_whenCreatingBooking_thenThrowException() {
        assertThatThrownBy(() -> bookingService.createBooking(bookingCreateDto, 99L))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    @Sql(statements = {
            RESET_USERS_ID,
            RESET_ITEMS_ID,
            RESET_BOOKINGS_ID,
            ADD_USER,
            ADD_USER_2,
            ADD_ITEM_NOT_AVAILABLE
    })
    void givenItemNotAvailable_whenCreatingBooking_thenThrowException() {
        assertThatThrownBy(() -> bookingService.createBooking(bookingCreateDto, 2L))
                .isInstanceOf(NotAvailableException.class);
    }

    @Test
    @Sql(statements = {
            RESET_USERS_ID,
            RESET_ITEMS_ID,
            RESET_BOOKINGS_ID,
            ADD_USER,
            ADD_USER_2,
            ADD_ITEM
    })
    void givenItem_whenCreatingBookingForOwnItem_thenThrowException() {
        assertThatThrownBy(() -> bookingService.createBooking(bookingCreateDto, 1L))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    @Sql(statements = {
            RESET_USERS_ID,
            RESET_ITEMS_ID,
            RESET_BOOKINGS_ID,
            ADD_USER,
            ADD_USER_2,
            ADD_ITEM,
    })
    void givenSavedBooking_whenConfirmingBooking_thenBookingIsConfirmed() {
        bookingService.createBooking(bookingCreateDto, 2L);
        var result = bookingService.confirmBooking(1L, 1L, Boolean.TRUE);
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getBooker().getId()).isEqualTo(2L);
        assertThat(result.getItem().getName()).isEqualTo("Item1");
        assertThat(result.getStatus()).isEqualTo(Status.APPROVED);
        assertThat(result.getStart().getHour()).isEqualTo(LocalDateTime.now().plusHours(1).getHour());
        assertThat(result.getEnd().getHour()).isEqualTo(LocalDateTime.now().plusHours(2).getHour());
    }

    @Test
    @Sql(statements = {
            RESET_USERS_ID,
            RESET_ITEMS_ID,
            RESET_BOOKINGS_ID,
            ADD_USER,
            ADD_USER_2,
            ADD_ITEM,
    })
    void givenBooking_whenApprovingAlreadyApprovedBooking_thenThrowException() {
        bookingService.createBooking(bookingCreateDto, 2L);
        bookingService.confirmBooking(1L, 1L, Boolean.TRUE);
        assertThatThrownBy(() -> bookingService.confirmBooking(1L, 1L, Boolean.TRUE))
                .isInstanceOf(BookingApprovalException.class);
    }

    @Test
    @Sql(statements = {
            RESET_USERS_ID,
            RESET_ITEMS_ID,
            RESET_BOOKINGS_ID,
            ADD_USER,
            ADD_USER_2,
            ADD_ITEM,
    })
    void givenBooking_whenApprovingNotOwnBooking_thenThrowException() {
        bookingService.createBooking(bookingCreateDto, 2L);
        assertThatThrownBy(() -> bookingService.confirmBooking(1L, 2L, Boolean.TRUE))
                .isInstanceOf(WrongUserException.class);
    }

    @Test
    @Sql(statements = {
            RESET_USERS_ID,
            RESET_ITEMS_ID,
            RESET_BOOKINGS_ID,
            ADD_USER,
            ADD_USER_2,
            ADD_ITEM,
            PAST_BOOKING,
            CURRENT_BOOKING,
            REJECTED_BOOKING,
            WAITING_BOOKING
    })
    void given4Bookings_whenBookerSearchersForPastBookings_thenFinds2() {
        var result = bookingService.findBookingsByBookerAndState(
                2L, State.PAST, PageRequest.of(0, 10)
        );
        assertThat(result.getBookings()).hasSize(2);
    }

    @Test
    @Sql(statements = {
            RESET_USERS_ID,
            RESET_ITEMS_ID,
            RESET_BOOKINGS_ID,
            ADD_USER,
            ADD_USER_2,
            ADD_ITEM,
            PAST_BOOKING,
            CURRENT_BOOKING,
            REJECTED_BOOKING,
            WAITING_BOOKING
    })
    void given4Bookings_whenBookerSearchersForFutureBookings_thenFinds1() {
        var result = bookingService.findBookingsByBookerAndState(
                2L, State.FUTURE, PageRequest.of(0, 10)
        );
        assertThat(result.getBookings()).hasSize(1);
    }

    @Test
    @Sql(statements = {
            RESET_USERS_ID,
            RESET_ITEMS_ID,
            RESET_BOOKINGS_ID,
            ADD_USER,
            ADD_USER_2,
            ADD_ITEM,
            PAST_BOOKING,
            CURRENT_BOOKING,
            REJECTED_BOOKING,
            WAITING_BOOKING
    })
    void given4Bookings_whenBookerSearchersForAllBookings_thenFinds4() {
        var result = bookingService.findBookingsByBookerAndState(
                2L, State.ALL, PageRequest.of(0, 10)
        );
        assertThat(result.getBookings()).hasSize(4);
    }

    @Test
    @Sql(statements = {
            RESET_USERS_ID,
            RESET_ITEMS_ID,
            RESET_BOOKINGS_ID,
            ADD_USER,
            ADD_USER_2,
            ADD_ITEM,
            PAST_BOOKING,
            CURRENT_BOOKING,
            REJECTED_BOOKING,
            WAITING_BOOKING
    })
    void given4Bookings_whenBookerSearchersForCurrentBookings_thenFinds1() {
        var result = bookingService.findBookingsByBookerAndState(
                2L, State.CURRENT, PageRequest.of(0, 10)
        );
        assertThat(result.getBookings()).hasSize(1);
    }

    @Test
    @Sql(statements = {
            RESET_USERS_ID,
            RESET_ITEMS_ID,
            RESET_BOOKINGS_ID,
            ADD_USER,
            ADD_USER_2,
            ADD_ITEM,
            PAST_BOOKING,
            CURRENT_BOOKING,
            REJECTED_BOOKING,
            WAITING_BOOKING
    })
    void given4Bookings_whenBookerSearchersForWaitingBookings_thenFinds1() {
        var result = bookingService.findBookingsByBookerAndState(
                2L, State.WAITING, PageRequest.of(0, 10)
        );
        assertThat(result.getBookings()).hasSize(1);
    }

    @Test
    @Sql(statements = {
            RESET_USERS_ID,
            RESET_ITEMS_ID,
            RESET_BOOKINGS_ID,
            ADD_USER,
            ADD_USER_2,
            ADD_ITEM,
            PAST_BOOKING,
            CURRENT_BOOKING,
            REJECTED_BOOKING,
            WAITING_BOOKING
    })
    void given4Bookings_whenBookerSearchersForRejectedBookings_thenFinds1() {
        var result = bookingService.findBookingsByBookerAndState(
                2L, State.REJECTED, PageRequest.of(0, 10)
        );
        assertThat(result.getBookings()).hasSize(1);
    }

    @Test
    @Sql(statements = {
            RESET_USERS_ID,
            RESET_ITEMS_ID,
            RESET_BOOKINGS_ID,
            ADD_USER,
            ADD_USER_2,
            ADD_ITEM,
            PAST_BOOKING,
            CURRENT_BOOKING,
            REJECTED_BOOKING,
            WAITING_BOOKING
    })
    void given4Bookings_whenNoBooker_thenThrowException() {
        assertThatThrownBy(() -> bookingService.findBookingsByBookerAndState(
                99L, State.REJECTED, PageRequest.of(0, 10)
        )).isInstanceOf(NotFoundException.class);
    }

    @Test
    @Sql(statements = {
            RESET_USERS_ID,
            RESET_ITEMS_ID,
            RESET_BOOKINGS_ID,
            ADD_USER,
            ADD_USER_2,
            ADD_ITEM,
            PAST_BOOKING,
            CURRENT_BOOKING,
            REJECTED_BOOKING,
            WAITING_BOOKING
    })
    void given4Bookings_whenOwnerSearchersForCurrentBookings_thenFinds1() {
        var result = bookingService.findBookingsByOwnerAndState(
                1L, State.CURRENT, PageRequest.of(0, 10)
        );
        assertThat(result.getBookings()).hasSize(1);
    }

    @Test
    @Sql(statements = {
            RESET_USERS_ID,
            RESET_ITEMS_ID,
            RESET_BOOKINGS_ID,
            ADD_USER,
            ADD_USER_2,
            ADD_ITEM,
            PAST_BOOKING,
            CURRENT_BOOKING,
            REJECTED_BOOKING,
            WAITING_BOOKING
    })
    void given4Bookings_whenOwnerSearchersForPastBookings_thenFinds2() {
        var result = bookingService.findBookingsByOwnerAndState(
                1L, State.PAST, PageRequest.of(0, 10)
        );
        assertThat(result.getBookings()).hasSize(2);
    }

    @Test
    @Sql(statements = {
            RESET_USERS_ID,
            RESET_ITEMS_ID,
            RESET_BOOKINGS_ID,
            ADD_USER,
            ADD_USER_2,
            ADD_ITEM,
            PAST_BOOKING,
            CURRENT_BOOKING,
            REJECTED_BOOKING,
            WAITING_BOOKING
    })
    void given4Bookings_whenOwnerSearchersForFutureBookings_thenFinds1() {
        var result = bookingService.findBookingsByOwnerAndState(
                1L, State.FUTURE, PageRequest.of(0, 10)
        );
        assertThat(result.getBookings()).hasSize(1);
    }

    @Test
    @Sql(statements = {
            RESET_USERS_ID,
            RESET_ITEMS_ID,
            RESET_BOOKINGS_ID,
            ADD_USER,
            ADD_USER_2,
            ADD_ITEM,
            PAST_BOOKING,
            CURRENT_BOOKING,
            REJECTED_BOOKING,
            WAITING_BOOKING
    })
    void given4Bookings_whenOwnerSearchersForAllBookings_thenFinds4() {
        var result = bookingService.findBookingsByOwnerAndState(
                1L, State.ALL, PageRequest.of(0, 10)
        );
        assertThat(result.getBookings()).hasSize(4);
    }

    @Test
    @Sql(statements = {
            RESET_USERS_ID,
            RESET_ITEMS_ID,
            RESET_BOOKINGS_ID,
            ADD_USER,
            ADD_USER_2,
            ADD_ITEM,
            PAST_BOOKING,
            CURRENT_BOOKING,
            REJECTED_BOOKING,
            WAITING_BOOKING
    })
    void given4Bookings_whenOwnerSearchersForWaitingBookings_thenFinds1() {
        var result = bookingService.findBookingsByOwnerAndState(
                1L, State.WAITING, PageRequest.of(0, 10)
        );
        assertThat(result.getBookings()).hasSize(1);
    }

    @Test
    @Sql(statements = {
            RESET_USERS_ID,
            RESET_ITEMS_ID,
            RESET_BOOKINGS_ID,
            ADD_USER,
            ADD_USER_2,
            ADD_ITEM,
            PAST_BOOKING,
            CURRENT_BOOKING,
            REJECTED_BOOKING,
            WAITING_BOOKING
    })
    void given4Bookings_whenOwnerSearchersForRejectedBookings_thenFinds1() {
        var result = bookingService.findBookingsByOwnerAndState(
                1L, State.REJECTED, PageRequest.of(0, 10)
        );
        assertThat(result.getBookings()).hasSize(1);
    }

    @Test
    @Sql(statements = {
            RESET_USERS_ID,
            RESET_ITEMS_ID,
            RESET_BOOKINGS_ID,
            ADD_USER,
            ADD_USER_2,
            ADD_ITEM,
            PAST_BOOKING,
            CURRENT_BOOKING,
            REJECTED_BOOKING,
            WAITING_BOOKING
    })
    void given4Bookings_whenNotOwner_thenThrowException() {
        assertThatThrownBy(() -> bookingService.findBookingsByBookerAndState(
                99L, State.REJECTED, PageRequest.of(0, 10)
        )).isInstanceOf(NotFoundException.class);
    }
}