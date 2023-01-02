package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingListDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.exceptions.BookingApprovalException;
import ru.practicum.shareit.booking.exceptions.NotAvailableException;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exceptions.GlobalExceptionHandler;
import ru.practicum.shareit.item.dto.ItemShortResponseDto;
import ru.practicum.shareit.item.exceptions.WrongUserException;
import ru.practicum.shareit.user.dto.UserIdDto;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = {BookingController.class, GlobalExceptionHandler.class})
@AutoConfigureMockMvc
class BookingControllerTest {

    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private BookingService bookingService;
    @Autowired
    private MockMvc mockMvc;

    private BookingResponseDto bookingResponseDto;
    private BookingCreateDto bookingCreateDto;

    @BeforeEach
    void setUp() {
        bookingResponseDto = BookingResponseDto.builder()
                .id(1L)
                .start(LocalDateTime.now().plusHours(1))
                .end(LocalDateTime.now().plusDays(1))
                .booker(new UserIdDto(1L))
                .item(new ItemShortResponseDto(1L, "Item"))
                .status(Status.WAITING)
                .build();

        bookingCreateDto = BookingCreateDto.builder()
                .itemId(1L)
                .start(LocalDateTime.now().plusHours(1))
                .end(LocalDateTime.now().plusDays(1))
                .build();
    }

    @Test
    void givenValidGetResourceRequest_whenFindBooking_thenStatusIsOk() throws Exception {
        when(bookingService.findBooking(anyLong(), anyLong())).thenReturn(bookingResponseDto);
        String body = objectMapper.writeValueAsString(bookingResponseDto);

        mockMvc.perform(
                        get("/bookings/1").header("X-Sharer-User-Id", 1)
                )
                .andExpect(status().isOk())
                .andExpect(content().json(body));
    }

    @Test
    void givenValidGetBookingByBookerRequest_whenFindingBookings_thenStatusIsOk() throws Exception {
        when(bookingService.findBookingsByBookerAndState(1L, State.ALL, PageRequest.of(0, 10)))
                .thenReturn(BookingListDto.builder().build());

        mockMvc.perform(
                        get("/bookings")
                                .header("X-Sharer-User-Id", 1)
                                .queryParam("state", "ALL")
                )
                .andExpect(status().isOk());
    }

    @Test
    void givenMissingHeaderRequest_thenThrowException() throws Exception {

        mockMvc.perform(
                        get("/bookings")
                                .queryParam("state", "ALL")
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenBadState_whenFindingBooking_thenBadRequest() throws Exception {
        mockMvc.perform(
                        get("/bookings").header("X-Sharer-User-Id", 1)
                                .queryParam("state", "UNSUPPORTED")
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenValidFindBookingsByOwnerRequest_whenFindingBookings_thenStatusIsOk() throws Exception {
        when(bookingService.findBookingsByOwnerAndState(1L, State.ALL, PageRequest.of(0, 10)))
                .thenReturn(BookingListDto.builder().build());

        mockMvc.perform(
                        get("/bookings/owner").header("X-Sharer-User-Id", 1)
                                .queryParam("state", "ALL")
                )
                .andExpect(status().isOk());
    }

    @Test
    void givenValidBookingCreateDto_whenCreatingBooking_thenStatusIsCreated() throws Exception {
        when(bookingService.createBooking(bookingCreateDto, 1L)).thenReturn(bookingResponseDto);
        final String createBody = objectMapper.writeValueAsString(bookingCreateDto);

        mockMvc.perform(
                        post("/bookings")
                                .header("X-Sharer-User-Id", 1)
                                .contentType("application/json")
                                .content(createBody)
                )
                .andExpect(status().isCreated());
    }

    @Test
    void givenItemNotAvailable_whenCreatingBooking_thenThrowException() throws Exception {
        when(bookingService.createBooking(bookingCreateDto, 1L)).thenThrow(NotAvailableException.class);
        final String createBody = objectMapper.writeValueAsString(bookingCreateDto);

        mockMvc.perform(
                        post("/bookings")
                                .header("X-Sharer-User-Id", 1)
                                .contentType("application/json")
                                .content(createBody)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenNewBookingStartInThePast_whenCreatingBooking_thenThrowValidationException() throws Exception {
        var bookingBadStartDto = BookingCreateDto.builder()
                .itemId(1L)
                .start(LocalDateTime.now().minusHours(1))
                .end(LocalDateTime.now().plusDays(1))
                .build();
        final String createBody = objectMapper.writeValueAsString(bookingBadStartDto);

        mockMvc.perform(
                        post("/bookings")
                                .header("X-Sharer-User-Id", 1)
                                .contentType("application/json")
                                .content(createBody)
                )
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(
                        result.getResolvedException() instanceof MethodArgumentNotValidException
                ));
    }

    @Test
    void givenNewBookingNullStart_whenCreatingBooking_thenThrowValidationException() throws Exception {
        ReflectionTestUtils.setField(bookingCreateDto, "start", null);
        final String createBody = objectMapper.writeValueAsString(bookingCreateDto);

        mockMvc.perform(
                        post("/bookings")
                                .header("X-Sharer-User-Id", 1)
                                .contentType("application/json")
                                .content(createBody)
                )
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(
                        result.getResolvedException() instanceof MethodArgumentNotValidException
                ));
    }

    @Test
    void givenValidConfirmBookingRequest_whenConfirmingBooking_thenStatusIsOk() throws Exception {
        ReflectionTestUtils.setField(bookingResponseDto, "status", Status.APPROVED);
        when(bookingService.confirmBooking(1L, 1L, Boolean.TRUE)).thenReturn(bookingResponseDto);

        mockMvc.perform(
                        patch("/bookings/1")
                                .header("X-Sharer-User-Id", 1)
                                .queryParam("approved", "true")
                )
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(bookingResponseDto)));
    }

    @Test
    void givenConfirmBookingRequest_whenConfirmingNotOwnBooking_thenThrowException() throws Exception {
        ReflectionTestUtils.setField(bookingResponseDto, "status", Status.APPROVED);
        when(bookingService.confirmBooking(1L, 1L, Boolean.TRUE)).thenThrow(WrongUserException.class);

        mockMvc.perform(
                        patch("/bookings/1")
                                .header("X-Sharer-User-Id", 1)
                                .queryParam("approved", "true")
                )
                .andExpect(status().isNotFound());
    }

    @Test
    void givenConfirmBookingRequest_whenConfirmingAlreadyConfirmed_thenThrowException() throws Exception {
        ReflectionTestUtils.setField(bookingResponseDto, "status", Status.APPROVED);
        when(bookingService.confirmBooking(1L, 1L, Boolean.TRUE)).thenThrow(BookingApprovalException.class);

        mockMvc.perform(
                        patch("/bookings/1")
                                .header("X-Sharer-User-Id", 1)
                                .queryParam("approved", "true")
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenConfirmBookingRequest_whenMissingApprovedParameter_thenThrowException() throws Exception {
        mockMvc.perform(
                        patch("/bookings/1")
                                .header("X-Sharer-User-Id", 1)
                )
                .andExpect(status().isBadRequest());
    }
}