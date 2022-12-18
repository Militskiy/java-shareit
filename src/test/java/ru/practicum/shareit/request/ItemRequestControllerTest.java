package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.exceptions.GlobalExceptionHandler;
import ru.practicum.shareit.item.dto.ResponseItemDto;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestCreateResponseDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDtoList;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {ItemRequestController.class, GlobalExceptionHandler.class})
@AutoConfigureMockMvc
class ItemRequestControllerTest {
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ItemRequestService itemRequestService;
    @Autowired
    private MockMvc mockMvc;
    private ItemRequestCreateDto itemRequestCreateDto;
    private ItemRequestCreateResponseDto itemRequestCreateResponseDto;
    private ItemRequestResponseDto itemRequestResponseDtoWithItem;
    private ItemRequestResponseDtoList responseDtoList;

    @BeforeEach
    void setUp() {
        itemRequestCreateDto = ItemRequestCreateDto.builder()
                .description("Item request")
                .build();
        itemRequestCreateResponseDto = ItemRequestCreateResponseDto.builder()
                .id(1L)
                .description("Item request")
                .created(LocalDateTime.now())
                .build();
        itemRequestResponseDtoWithItem = ItemRequestResponseDto.builder()
                .id(1L)
                .description("Item request")
                .created(LocalDateTime.now())
                .items(Set.of(ResponseItemDto.builder()
                        .id(1L)
                        .name("Item")
                        .description("Test item")
                        .available(Boolean.TRUE)
                        .requestId(1L)
                        .build()))
                .build();
        responseDtoList = ItemRequestResponseDtoList.builder()
                .itemRequests(List.of(itemRequestResponseDtoWithItem))
                .build();
    }

    @Test
    void givenValidCreateDto_whenSendingPostRequest_thenStatusIsOk() throws Exception {
        when(itemRequestService.create(itemRequestCreateDto, 1L)).thenReturn(itemRequestCreateResponseDto);

        final String request = objectMapper.writeValueAsString(itemRequestCreateDto);
        final String response = objectMapper.writeValueAsString(itemRequestCreateResponseDto);

        this.mockMvc.perform(
                        post("/requests")
                                .header("X-Sharer-User-Id", 1L)
                                .contentType("application/json")
                                .content(request)
                )
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    void givenValidGetRequestsRequest_thenStatusIsOk() throws Exception {
        when(itemRequestService.findOwnRequests(
                1L, PageRequest.of(0, 10, Sort.by("created").descending())
        )).thenReturn(responseDtoList);

        final String response = objectMapper.writeValueAsString(responseDtoList);

        this.mockMvc.perform(
                        get("/requests")
                                .header("X-Sharer-User-Id", 1L)
                )
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    void givenValidGetAllRequest_thenStatusIsOk() throws Exception {
        when(itemRequestService.findAllRequests(
                1L, PageRequest.of(0, 10, Sort.by("created").descending())
        )).thenReturn(responseDtoList);

        final String response = objectMapper.writeValueAsString(responseDtoList);

        this.mockMvc.perform(
                        get("/requests/all")
                                .header("X-Sharer-User-Id", 1L)
                )
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    void givenValidGetByIdRequest_thenStatusIsOk() throws Exception {
        when(itemRequestService.findById(1L, 1L)).thenReturn(itemRequestResponseDtoWithItem);

        final String response = objectMapper.writeValueAsString(itemRequestResponseDtoWithItem);

        this.mockMvc.perform(
                        get("/requests/1")
                                .header("X-Sharer-User-Id", 1L)
                )
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }
}