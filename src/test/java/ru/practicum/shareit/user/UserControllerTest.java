package ru.practicum.shareit.user;

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
import ru.practicum.shareit.exceptions.GlobalExceptionHandler;
import ru.practicum.shareit.user.dto.ResponseUserDto;
import ru.practicum.shareit.user.dto.UpdateUserDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserListDto;
import ru.practicum.shareit.user.service.UserService;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {UserController.class, GlobalExceptionHandler.class})
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    UserService userService;
    @Autowired
    private MockMvc mockMvc;

    private ResponseUserDto responseUserDto;
    private UserDto userDto;
    private UpdateUserDto updateUserDto;

    @BeforeEach
    void setUp() {
        responseUserDto = new ResponseUserDto(1L, "User", "user@email.com");
        userDto = UserDto.builder()
                .name("User")
                .email("user@email.com")
                .build();
        updateUserDto = UpdateUserDto.builder().name("Updated user").build();
    }

    @Test
    void givenValidFindAllGetRequest_thenStatusIsOk() throws Exception {
        when(userService.findAllUsers(PageRequest.of(0, 10))).thenReturn(UserListDto.builder().build());

        this.mockMvc.perform(
                        get("/users")
                )
                .andExpect(status().isOk());
    }

    @Test
    void givenValidFindUserRequest_thenStatusIsOk() throws Exception {
        when(userService.findUser(anyLong())).thenReturn(responseUserDto);
        final String responseBody = objectMapper.writeValueAsString(responseUserDto);

        this.mockMvc.perform(
                        get("/users/1")
                )
                .andExpect(status().isOk())
                .andExpect(content().json(responseBody));
    }

    @Test
    void givenValidUserDto_whenCreatingUser_thenStatusIsOk() throws Exception {
        final String createDtoBody = objectMapper.writeValueAsString(userDto);
        final String responseBody = objectMapper.writeValueAsString(responseUserDto);
        when(userService.createUser(userDto)).thenReturn(responseUserDto);
        this.mockMvc.perform(
                        post("/users")
                                .contentType("application/json")
                                .content(createDtoBody)
                )
                .andExpect(status().isCreated())
                .andExpect(content().json(responseBody));
    }

    @Test
    void givenValidUpdateUserDto_whenUpdatingUser_thenStatusIsOk() throws Exception {
        ReflectionTestUtils.setField(responseUserDto, "name", "Updated user");
        final String updateDtoBody = objectMapper.writeValueAsString(updateUserDto);
        final String responseBody = objectMapper.writeValueAsString(responseUserDto);

        when(userService.updateUser(updateUserDto, 1L)).thenReturn(responseUserDto);

        this.mockMvc.perform(
                        patch("/users/1")
                                .contentType("application/json")
                                .content(updateDtoBody)
                )
                .andExpect(status().isOk())
                .andExpect(content().json(responseBody));
    }

    @Test
    void givenDeleteRequest_whenDeletingUser_thenStatusIsOk() throws Exception {
        this.mockMvc.perform(
                        delete("/users/1")
                )
                .andExpect(status().isOk());
    }
}