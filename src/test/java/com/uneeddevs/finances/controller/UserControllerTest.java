package com.uneeddevs.finances.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uneeddevs.finances.controller.exception.ValidationError;
import com.uneeddevs.finances.dto.UserInsertDTO;
import com.uneeddevs.finances.dto.UserUpdateDTO;
import com.uneeddevs.finances.model.User;
import com.uneeddevs.finances.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.persistence.NoResultException;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Test
    void testFindUserByIdExpectedNotFoundStatus() throws Exception {
        when(userService.findById(any(UUID.class))).thenThrow(new NoResultException("Not found"));
        mockMvc.perform(get("/users/{uuid}", "3fa85f64-5717-4562-b3fc-2c963f66afa6"))
                .andExpect(status().isNotFound());
        verify(userService).findById(any(UUID.class));
    }

    @Test
    void testFindUserByIdExpectedSuccessStatus() throws Exception {
        User user = new User("name", "email@mail.com", "password");
        final Class<User> userClass = User.class;
        Field idField = userClass.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(user, UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"));
        Field registerDateField = userClass.getDeclaredField("registerDate");
        registerDateField.setAccessible(true);
        registerDateField.set(user, LocalDateTime.now());
        when(userService.findById(any(UUID.class))).thenReturn(user);
        mockMvc.perform(get("/users/{uuid}", "3fa85f64-5717-4562-b3fc-2c963f66afa6"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(user)));
        verify(userService).findById(any(UUID.class));
    }

    @Test
    void testNewUserInsertExpectedCreatedStatus() throws Exception {
        String email = "email@mail.com";
        UserInsertDTO userInsert = new UserInsertDTO("name", email, "password");
        User userResponse = new User("name", email, "password");
        final Class<User> userClass = User.class;
        Field idField = userClass.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(userResponse, UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa7"));
        Field registerDateField = userClass.getDeclaredField("registerDate");
        registerDateField.setAccessible(true);
        registerDateField.set(userResponse, LocalDateTime.now());

        when(userService.findByEmail(email)).thenThrow(new NoResultException("No user with email " + email));

        mockMvc.perform(post("/users", "3fa85f64-5717-4562-b3fc-2c963f66afa6")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userInsert)))
                .andExpect(status().isCreated());
    }

    @Test
    void testNewUserInsertWithExistentEmailExpectedBadRequest() throws Exception {
        String email = "email@mail.com";
        UserInsertDTO userInsert = new UserInsertDTO("name", email, "password");

        ValidationError validationError = new ValidationError(LocalDateTime.now(), 400,
                "Bad request",
                "Validation error",
                "/users");
        validationError.addError("email", "Email already registered");
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userInsert)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(objectMapper.writeValueAsString(validationError)));
    }

    @ParameterizedTest
    @MethodSource(value = "badRequestMethodSource")
    void testNewUserInsertBlankFieldsExpectedBadRequest(String email, String name, String password, String fieldName) throws Exception {
        UserInsertDTO userInsert = new UserInsertDTO(name, email, password);
        when(userService.findByEmail(email)).thenThrow(new NoResultException("No user with email " + email));
        ValidationError validationError = new ValidationError(LocalDateTime.now(), 400,
                "Bad request",
                "Validation error",
                "/users");
        StringBuilder sb = new StringBuilder(fieldName);
        sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
        validationError.addError(fieldName,  sb.toString() + " is mandatory");
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userInsert)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(objectMapper.writeValueAsString(validationError)));
    }

    private static Stream<Arguments> badRequestMethodSource() {
        return Stream.of(
                Arguments.of(null, "name", "password", "email"),
                Arguments.of("email@mail.com", "", "password", "name"),
                Arguments.of("email@mail.com", "name", "", "password")
        );
    }

    @Test
    void testUpdateUserExpectedCreatedStatus() throws Exception {
        String uuid = "3fa85f64-5717-4562-b3fc-2c963f66afa6";
        UserUpdateDTO userUpdate = new UserUpdateDTO("name", "secret123");
        User userResponse = new User("name", "user@email.com", "secret123");
        final Class<User> userClass = User.class;
        Field idField = userClass.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(userResponse, UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa7"));
        Field registerDateField = userClass.getDeclaredField("registerDate");
        registerDateField.setAccessible(true);
        registerDateField.set(userResponse, LocalDateTime.now());

        mockMvc.perform(put("/users/{uuid}", uuid)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userUpdate)))
                .andExpect(status().isOk());
    }

    @ParameterizedTest
    @MethodSource(value = "badRequestUpdateMethodSource")
    void testUpdateUserBlankFieldsExpectedBadRequest(String name, String password, String fieldName) throws Exception {
        String uuid = "3fa85f64-5717-4562-b3fc-2c963f66afa6";
        UserUpdateDTO userUpdate = new UserUpdateDTO(name, password);
        ValidationError validationError = new ValidationError(LocalDateTime.now(), 400,
                "Bad request",
                "Validation error",
                "/users/" + uuid);
        StringBuilder sb = new StringBuilder(fieldName);
        sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
        validationError.addError(fieldName,  sb + " is mandatory");
        mockMvc.perform(put("/users/{uuid}", uuid)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userUpdate)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(objectMapper.writeValueAsString(validationError)));
    }

    private static Stream<Arguments> badRequestUpdateMethodSource() {
        return Stream.of(
                Arguments.of("", "password", "name"),
                Arguments.of("name", "", "password")
        );
    }

    @Test
    void testRequestPageExpectedOkStatus() throws Exception{

        User userResponse = new User("name", "user@email.com", "password");
        final Class<User> userClass = User.class;
        Field idField = userClass.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(userResponse, UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa7"));
        Field registerDateField = userClass.getDeclaredField("registerDate");
        registerDateField.setAccessible(true);
        registerDateField.set(userResponse, LocalDateTime.now());

        List<User> users = new ArrayList<>();
        users.add(userResponse);
        Page<User> page = new PageImpl<>(users);
        when(userService.findPage(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/users/page"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(page)));

        verify(userService).findPage(any(Pageable.class));
    }

    @Test
    void testRequestPageExpectedNotFoundStatus() throws Exception{
        
        when(userService.findPage(any(Pageable.class))).thenThrow(new NoResultException("No result in page"));

        mockMvc.perform(get("/users/page"))
                .andExpect(status().isNotFound());

        verify(userService).findPage(any(Pageable.class));
    }
}
