package ru.practicum.shareit.item.service;

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
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;
import ru.practicum.shareit.item.exceptions.CommentException;
import ru.practicum.shareit.item.exceptions.WrongUserException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@ActiveProfiles("test")
@Transactional
class ItemServiceTest implements Queries {
    @Autowired
    private ItemService itemService;
    private ItemDto itemOne;
    private ItemDto itemTwo;
    private UpdateItemDto updateItem;

    @BeforeEach
    void setUp() {
        itemOne = ItemDto.builder()
                .name("Item1")
                .description("Test item1")
                .available(Boolean.TRUE)
                .build();
        itemTwo = ItemDto.builder()
                .name("Item2")
                .description("Test item2")
                .available(Boolean.TRUE)
                .build();
        updateItem = UpdateItemDto.builder()
                .available(Boolean.FALSE)
                .build();
    }


    @Test
    @Sql(statements = {RESET_IDS, ADD_USER})
    void givenSavedItem_whenFindingItem_thenItemIsFound() {
        var createResult = itemService.createItem(itemOne, 1L);
        var findResult = itemService.findItem(createResult.getId(), 1L);
        assertThat(findResult).usingRecursiveComparison()
                .ignoringFields("comments", "nextBooking", "lastBooking")
                .isEqualTo(createResult);
    }

    @Test
    void givenNoItem_whenFindingItem_thenThrowException() {
        assertThatThrownBy(() -> itemService.findItem(99L, 99L)).isInstanceOf(NotFoundException.class);
    }

    @Test
    void givenNoOwner_whenFindingItem_thenThrowException() {
        assertThatThrownBy(() -> itemService.findAllItems(99L, PageRequest.of(0, 10)))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void givenNoOwner_whenCreatingItem_thenThrowException() {
        assertThatThrownBy(() -> itemService.createItem(itemOne, 99L))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    @Sql(statements = {RESET_IDS, ADD_USER})
    void givenTwoSavedItems_whenFindingAllItems_thenAllItemsAreFound() {
        itemService.createItem(itemOne, 1L);
        itemService.createItem(itemTwo, 1L);
        var result = itemService.findAllItems(1L, PageRequest.of(0, 10));
        assertThat(result.getItems()).hasSize(2);
        assertThat(result.getItems().get(0))
                .usingRecursiveComparison()
                .ignoringFields("id", "comments", "nextBooking", "lastBooking")
                .isEqualTo(itemOne);
        assertThat(result.getItems().get(1))
                .usingRecursiveComparison()
                .ignoringFields("id", "comments", "nextBooking", "lastBooking")
                .isEqualTo(itemTwo);
    }

    @Test
    @Sql(statements = {RESET_IDS, ADD_USER})
    void givenValidItemDto_whenCreatingItem_thenItemIsCreated() {
        var result = itemService.createItem(itemOne, 1L);
        assertThat(result)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(itemOne);
        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    @Sql(statements = {RESET_IDS, ADD_USER})
    void givenValidUpdateDto_whenUpdatingItem_thenItemIsUpdated() {
        var createResult = itemService.createItem(itemOne, 1L);
        var updateResult = itemService.updateItem(updateItem, 1L, 1L);
        assertThat(updateResult)
                .usingRecursiveComparison()
                .ignoringFields("available")
                .isEqualTo(createResult);
        assertThat(updateResult.getAvailable()).isEqualTo(updateItem.getAvailable());

    }

    @Test
    @Sql(statements = {RESET_IDS, ADD_USER})
    void givenValidUpdateDtoButWrongOwner_whenUpdatingItem_thenThrowException() {
        itemService.createItem(itemOne, 1L);
        assertThatThrownBy(() -> itemService.updateItem(updateItem, 1L, 99L))
                .isInstanceOf(WrongUserException.class);
    }

    @Test
    @Sql(statements = {RESET_IDS, ADD_USER})
    void givenValidUpdateDtoButItemId_whenUpdatingItem_thenThrowException() {
        itemService.createItem(itemOne, 1L);
        assertThatThrownBy(() -> itemService.updateItem(updateItem, 99L, 1L))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    @Sql(statements = {RESET_IDS, ADD_USER})
    void givenSavedItem_whenDeletingItem_itemIsDeleted() {
        var id = itemService.createItem(itemOne, 1L).getId();
        assertThat(itemService.findAllItems(1L, PageRequest.of(0, 10)).getItems()).hasSize(1);
        itemService.deleteItem(id);
        assertThat(itemService.findAllItems(1L, PageRequest.of(0, 10)).getItems()).hasSize(0);
    }

    @Test
    @Sql(statements = {RESET_IDS, ADD_USER})
    void givenSearchRequest_whenSearching_thenFindResultByNamen() {
        itemService.createItem(itemOne, 1L);
        itemService.createItem(itemTwo, 1L);
        var result = itemService.searchItems("iTeM", PageRequest.of(0, 10));
        assertThat(result.getItems()).hasSize(2);
        assertThat(result.getItems().get(0))
                .usingRecursiveComparison()
                .ignoringFields("id", "comments", "nextBooking", "lastBooking")
                .isEqualTo(itemOne);
        assertThat(result.getItems().get(1))
                .usingRecursiveComparison()
                .ignoringFields("id", "comments", "nextBooking", "lastBooking")
                .isEqualTo(itemTwo);
    }

    @Test
    @Sql(statements = {RESET_IDS, ADD_USER})
    void givenSearchRequest_whenSearching_thenFindResultByDescription() {
        itemService.createItem(itemOne, 1L);
        itemService.createItem(itemTwo, 1L);
        var result = itemService.searchItems("TeSt", PageRequest.of(0, 10));
        assertThat(result.getItems()).hasSize(2);
        assertThat(result.getItems().get(0))
                .usingRecursiveComparison()
                .ignoringFields("id", "comments", "nextBooking", "lastBooking")
                .isEqualTo(itemOne);
        assertThat(result.getItems().get(1))
                .usingRecursiveComparison()
                .ignoringFields("id", "comments", "nextBooking", "lastBooking")
                .isEqualTo(itemTwo);
    }

    @Test
    @Sql(statements = {RESET_IDS, ADD_USER})
    void givenEmptySearchRequest_whenSearching_thenFindEmptyResult() {
        itemService.createItem(itemOne, 1L);
        var result = itemService.searchItems("", PageRequest.of(0, 10));
        assertThat(result.getItems()).hasSize(0);
    }

    @Test
    @Sql(statements = {RESET_IDS, ADD_USER, ADD_USER_2, ADD_ITEM, ADD_BOOKING_ITEM1_USER2})
    void givenSavedItemWithPreviousBookingByUser2_whenCommentingItem_thenCommentIsCreated() {
        var result = itemService.commentItem(2L, 1L, CommentCreateDto.builder().text("Comment").build());
        assertThat(result.getText()).isEqualTo("Comment");
        var commentResult = itemService.findItem(1L, 1L);
        assertThat(commentResult.getComments()).contains(result);
    }

    @Test
    @Sql(statements = {RESET_IDS, ADD_USER, ADD_USER_2, ADD_ITEM, ADD_BOOKING_ITEM1_USER2})
    void givenSavedItemWithPreviousBookingByUser2_whenCommentingItemWithWrongUser_thenThrowException() {
        assertThatThrownBy(() -> itemService.commentItem(
                99L, 1L, CommentCreateDto.builder().text("Comment").build()
        )).isInstanceOf(CommentException.class);
    }
}