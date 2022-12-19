package ru.practicum.shareit.item;

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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.exceptions.GlobalExceptionHandler;
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemListDto;
import ru.practicum.shareit.item.dto.ItemListWithBookingsDto;
import ru.practicum.shareit.item.dto.ItemWithBookingsDto;
import ru.practicum.shareit.item.dto.ResponseItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;
import ru.practicum.shareit.item.exceptions.CommentException;
import ru.practicum.shareit.item.service.ItemService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {ItemController.class, GlobalExceptionHandler.class})
@AutoConfigureMockMvc
class ItemControllerTest {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ItemService itemService;

    private ItemDto itemCreateDtoOne;

    private ResponseItemDto responseItemDtoOne;

    private ItemListWithBookingsDto itemListWithBookingsDto;
    private ItemListDto itemListDto;
    private ItemWithBookingsDto itemWithBookingsDtoOne;
    private UpdateItemDto updateItemDto;
    private CommentCreateDto commentCreateDto;
    private CommentResponseDto commentResponseDto;

    @BeforeEach
    void setUp() {
        itemCreateDtoOne = ItemDto.builder()
                .available(Boolean.TRUE)
                .name("Item1")
                .description("Description1")
                .build();

        responseItemDtoOne = ResponseItemDto.builder()
                .available(Boolean.TRUE)
                .name("Item1")
                .description("Description1")
                .build();

        responseItemDtoOne = ResponseItemDto.builder()
                .available(Boolean.TRUE)
                .name("Item2")
                .description("Description2")
                .build();

        commentCreateDto = CommentCreateDto.builder().text("Comment1").build();
        commentResponseDto = CommentResponseDto.builder()
                .id(1L)
                .text("Comment1")
                .authorName("User1")
                .created(LocalDateTime.now())
                .build();

        itemWithBookingsDtoOne = ItemWithBookingsDto.builder()
                .id(1L)
                .name("Item1")
                .description("Description1")
                .available(Boolean.TRUE)
                .lastBooking(new BookingShortDto(1L, 1L))
                .nextBooking(new BookingShortDto(2L, 1L))
                .comments(Set.of(commentResponseDto))
                .build();

        itemListWithBookingsDto = ItemListWithBookingsDto.builder()
                .items(List.of(itemWithBookingsDtoOne))
                .build();

        itemListDto = ItemListDto.builder().items(List.of(responseItemDtoOne)).build();

        updateItemDto = UpdateItemDto.builder().available(Boolean.FALSE).build();
    }

    @Test
    void givenValidFindAllItemsRequest_whenFindingAllItems_thenStatusIsOk() throws Exception {
        when(itemService.findAllItems(1L, PageRequest.of(0, 10, Sort.by("id").ascending())))
                .thenReturn(itemListWithBookingsDto);

        final String body = objectMapper.writeValueAsString(itemListWithBookingsDto);

        this.mockMvc.perform(
                        get("/items")
                                .header("X-Sharer-User-Id", 1L)
                                .queryParam("from", "0")
                                .queryParam("size", "10")
                )
                .andExpect(status().isOk())
                .andExpect(content().json(body));
    }

    @Test
    void givenValidFindItemRequest_whenFindingItem_thenStatusIsOk() throws Exception {
        when(itemService.findItem(anyLong(), anyLong())).thenReturn(itemWithBookingsDtoOne);

        final String body = objectMapper.writeValueAsString(itemWithBookingsDtoOne);

        this.mockMvc.perform(
                        get("/items/1")
                                .header("X-Sharer-User-Id", 1L)
                )
                .andExpect(status().isOk())
                .andExpect(content().json(body));
    }

    @Test
    void givenValidSearchItemRequest_whenSearchingItem_thenStatusIsOk() throws Exception {
        when(
                itemService.searchItems("item".toUpperCase(), PageRequest.of(0, 10, Sort.by("id")
                        .ascending()))
        )
                .thenReturn(itemListDto);

        final String body = objectMapper.writeValueAsString(itemListDto);

        this.mockMvc.perform(
                        get("/items/search")
                                .queryParam("text", "item")
                                .queryParam("from", "0")
                                .queryParam("size", "10")
                )
                .andExpect(status().isOk())
                .andExpect(content().json(body));
    }

    @Test
    void givenInvalidSearchItemRequest_whenSearchingItem_thenBadRequest() throws Exception {
        this.mockMvc.perform(
                        get("/items/search")
                                .queryParam("text", "item")
                                .queryParam("from", "-1")
                                .queryParam("size", "10")
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenValidItemCreateDto_whenCreatingItem_thenStatusIsCreated() throws Exception {
        when(itemService.createItem(itemCreateDtoOne, 1L)).thenReturn(responseItemDtoOne);

        final String responseBody = objectMapper.writeValueAsString(responseItemDtoOne);

        final String requestBody = objectMapper.writeValueAsString(itemCreateDtoOne);

        this.mockMvc.perform(
                        post("/items")
                                .header("X-Sharer-User-Id", 1L)
                                .content(requestBody)
                                .contentType("application/json")
                )
                .andExpect(status().isCreated())
                .andExpect(content().json(responseBody));
    }

    @Test
    void givenValidUpdateItemDto_whenUpdatingItem_thenStatusIsOk() throws Exception {
        ResponseItemDto updatedResponse = responseItemDtoOne.toBuilder().available(Boolean.FALSE).build();

        when(itemService.updateItem(updateItemDto, 1L, 1L)).thenReturn(updatedResponse);

        final String requestBody = objectMapper.writeValueAsString(updateItemDto);

        final String responseBody = objectMapper.writeValueAsString(updatedResponse);

        this.mockMvc.perform(
                        patch("/items/1")
                                .header("X-Sharer-User-Id", 1L)
                                .content(requestBody)
                                .contentType("application/json")
                )
                .andExpect(status().isOk())
                .andExpect(content().json(responseBody));
    }

    @Test
    void givenDeleteResourceRequest_whenDeleting_thenStatusIsOk() throws Exception {
        this.mockMvc.perform(
                        delete("/items/1")
                                .header("X-Sharer-User-Id", 1L)
                )
                .andExpect(status().isOk());
    }

    @Test
    void givenValidCommentCreateDto_whenCommentingItem_thenGetCommentResponseDto() throws Exception {
        when(itemService.commentItem(1L, 1L, commentCreateDto)).thenReturn(commentResponseDto);

        final String requestBody = objectMapper.writeValueAsString(commentCreateDto);
        final String responseBody = objectMapper.writeValueAsString(commentResponseDto);

        this.mockMvc.perform(
                        post("/items/1/comment")
                                .header("X-Sharer-User-Id", 1L)
                                .content(requestBody)
                                .contentType("application/json")
                )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(responseBody));
    }

    @Test
    void givenCommentCreateDto_whenCommentingItemWithoutBooking_thenGetBadRequest() throws Exception {
        when(itemService.commentItem(1L, 1L, commentCreateDto)).thenThrow(CommentException.class);

        final String requestBody = objectMapper.writeValueAsString(commentCreateDto);

        this.mockMvc.perform(
                        post("/items/1/comment")
                                .header("X-Sharer-User-Id", 1L)
                                .content(requestBody)
                                .contentType("application/json")
                )
                .andExpect(status().isBadRequest());
    }
}