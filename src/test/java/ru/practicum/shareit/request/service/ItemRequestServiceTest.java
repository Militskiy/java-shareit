package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.Queries;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestCreateResponseDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@ActiveProfiles("test")
@Transactional
class ItemRequestServiceTest implements Queries {
    @Autowired
    private ItemRequestService itemRequestService;
    private ItemRequestCreateDto itemRequestCreateDto;
    private ItemRequestCreateResponseDto itemRequestCreateResponseDto;

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
    }

    @Test
    @Sql(statements = {RESET_IDS, ADD_USER})
    void givenValidItemRequestCreateDto_whenCreatingItemRequest_thenItemRequestIsCreated() {
        var result = itemRequestService.create(itemRequestCreateDto, 1L);
        assertThat(result)
                .usingRecursiveComparison()
                .ignoringFields("created")
                .isEqualTo(itemRequestCreateResponseDto);
    }

    @Test
    void givenNoUser_whenCreatingItemRequest_thenThrowException() {
        assertThatThrownBy(() -> itemRequestService.create(itemRequestCreateDto, 99L))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void givenNoUser_whenFindingById_thenThrowException() {
        assertThatThrownBy(() -> itemRequestService.findById(1L, 1L))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    @Sql(statements = {RESET_IDS, ADD_USER})
    void givenNoRequest_whenFindingById_thenThrowException() {
        assertThatThrownBy(() -> itemRequestService.findById(1L, 1L))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    @Sql(statements = {RESET_IDS, ADD_USER})
    void givenSavedItemRequest_whenFindingById_thenRequestIsFound() {
        var createResult = itemRequestService.create(itemRequestCreateDto, 1L);
        var findResult = itemRequestService.findById(1L, 1L);
        assertThat(findResult)
                .usingRecursiveComparison()
                .ignoringFields("items")
                .isEqualTo(createResult);
    }

    @Test
    @Sql(statements = {RESET_IDS, ADD_USER})
    void givenSavedItemRequest_whenFindingOwnRequests_thenRequestsAreFound() {
        var createResult = itemRequestService.create(itemRequestCreateDto, 1L);
        var findResult = itemRequestService.findOwnRequests(1L, PageRequest.of(0, 10));
        assertThat(findResult.getItemRequests()).hasSize(1);
        assertThat(findResult.getItemRequests().get(0))
                .usingRecursiveComparison()
                .ignoringFields("items")
                .isEqualTo(createResult);
    }

    @Test
    void givenNoUser_whenFindingOwnRequests_thenThrowException() {
        assertThatThrownBy(() -> itemRequestService.findOwnRequests(1L, PageRequest.of(0, 10)))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    @Sql(statements = {RESET_IDS, ADD_USER, ADD_USER_2})
    void givenSavedItemRequest_whenFindingAll_thenRequestsAreFound() {
        var createResult = itemRequestService.create(itemRequestCreateDto, 1L);
        var findResult = itemRequestService.findAllRequests(2L, PageRequest.of(0, 10));
        assertThat(findResult.getItemRequests()).hasSize(1);
        assertThat(findResult.getItemRequests().get(0))
                .usingRecursiveComparison()
                .ignoringFields("items")
                .isEqualTo(createResult);
    }

    @Test
    @Sql(statements = {RESET_IDS, ADD_USER})
    void givenOnlyOwnRequest_whenFindingAll_thenNoRequestsAreFound() {
        var findResult = itemRequestService.findAllRequests(1L, PageRequest.of(0, 10));
        assertThat(findResult.getItemRequests()).hasSize(0);
    }

    @Test
    void givenNoUser_whenFindingAllRequests_thenThrowException() {
        assertThatThrownBy(() -> itemRequestService.findAllRequests(1L, PageRequest.of(0, 10)))
                .isInstanceOf(NotFoundException.class);
    }
}