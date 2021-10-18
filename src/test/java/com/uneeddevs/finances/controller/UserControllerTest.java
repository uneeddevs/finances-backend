package com.uneeddevs.finances.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uneeddevs.finances.config.PasswordManagerConfig;
import com.uneeddevs.finances.config.SecurityConfig;
import com.uneeddevs.finances.controller.exception.ValidationError;
import com.uneeddevs.finances.dto.UserInsertDTO;
import com.uneeddevs.finances.dto.UserUpdateDTO;
import com.uneeddevs.finances.mocks.UserMock;
import com.uneeddevs.finances.model.User;
import com.uneeddevs.finances.security.SecurityMock;
import com.uneeddevs.finances.security.exception.AuthenticationFailException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import javax.persistence.NoResultException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(value = UserController.class)
@Import({SecurityConfig.class, PasswordManagerConfig.class})
class UserControllerTest extends SecurityMock {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private final LocalDateTime defaultLocalDateTime = LocalDateTime.of(2020, 1, 1, 12, 0);

    @Test
    @WithMockUser(roles = "ADMIN")
    void testFindUserByIdExpectedNotFoundStatus() throws Exception {
        when(userService.findById(any(UUID.class))).thenThrow(new NoResultException("Not found"));
        mockMvc.perform(get("/users/{uuid}", "3fa85f64-5717-4562-b3fc-2c963f66afa6"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        verify(userService).findById(any(UUID.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testFindUserByIdExpectedSuccessStatus() throws Exception {
        User user = UserMock.mock(true);
        when(userService.findById(any(UUID.class))).thenReturn(user);
        mockMvc.perform(get("/users/{uuid}", "3fa85f64-5717-4562-b3fc-2c963f66afa6"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(user.toUserResponseDTO())));
        verify(userService).findById(any(UUID.class));
    }

    @Test
    @WithMockUser(roles = "USER")
    void testFindUserByIdExpectedForbiddenStatus() throws Exception {
        when(userService.findById(any(UUID.class))).thenThrow(new AuthenticationFailException("Forbidden"));
        mockMvc.perform(get("/users/{uuid}", "3fa85f64-5717-4562-b3fc-2c963f66afa6"))
                .andExpect(status().isForbidden());
        verify(userService).findById(any(UUID.class));
    }

    @Test
    void testNewUserInsertExpectedCreatedStatus() throws Exception {
        try(MockedStatic<LocalDateTime> mockedLocalDateTime = Mockito.mockStatic(LocalDateTime.class)) {
            mockedLocalDateTime.when(LocalDateTime::now).thenReturn(defaultLocalDateTime);
            String email = "email@mail.com";
            UserInsertDTO userInsert = new UserInsertDTO("name", email, "password");

            when(userService.findByEmail(email)).thenThrow(new NoResultException("No user with email " + email));

            mockMvc.perform(post("/users", "3fa85f64-5717-4562-b3fc-2c963f66afa6")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(userInsert)))
                    .andExpect(status().isCreated());
        }
    }

    @Test
    @WithMockUser(roles = "USER")
    void testNewUserInsertWithExistentEmailExpectedBadRequest() throws Exception {
        try(MockedStatic<LocalDateTime> mockedLocalDateTime = Mockito.mockStatic(LocalDateTime.class)) {
            mockedLocalDateTime.when(LocalDateTime::now).thenReturn(defaultLocalDateTime);
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
    }

    @ParameterizedTest
    @MethodSource(value = "badRequestMethodSource")
    void testNewUserInsertBlankFieldsExpectedBadRequest(String email, String name, String password, String fieldName) throws Exception {
        try(MockedStatic<LocalDateTime> mockedLocalDateTime = Mockito.mockStatic(LocalDateTime.class)) {
            mockedLocalDateTime.when(LocalDateTime::now).thenReturn(defaultLocalDateTime);
            UserInsertDTO userInsert = new UserInsertDTO(name, email, password);
            when(userService.findByEmail(email)).thenThrow(new NoResultException("No user with email " + email));
            ValidationError validationError = new ValidationError(LocalDateTime.now(), 400,
                    "Bad request",
                    "Validation error",
                    "/users");
            StringBuilder sb = new StringBuilder(fieldName);
            sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
            validationError.addError(fieldName, sb + " is mandatory");
            mockMvc.perform(post("/users")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(userInsert)))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().json(objectMapper.writeValueAsString(validationError)));
        }
    }

    private static Stream<Arguments> badRequestMethodSource() {
        return Stream.of(
                Arguments.of(null, "name", "password", "email"),
                Arguments.of("email@mail.com", "", "password", "name"),
                Arguments.of("email@mail.com", "name", "", "password")
        );
    }

    @Test
    @WithMockUser(roles = "USER")
    void testUpdateUserExpectedOkStatus() throws Exception {
        try(MockedStatic<LocalDateTime> mockedLocalDateTime = Mockito.mockStatic(LocalDateTime.class)) {
            mockedLocalDateTime.when(LocalDateTime::now).thenReturn(defaultLocalDateTime);
            String uuid = "3fa85f64-5717-4562-b3fc-2c963f66afa6";
            UserUpdateDTO userUpdate = new UserUpdateDTO("name", "secret123");

            mockMvc.perform(put("/users/{uuid}", uuid)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(userUpdate)))
                    .andExpect(status().isOk());
        }
    }

    @ParameterizedTest
    @WithMockUser(roles = "USER")
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
    @WithMockUser(roles = "ADMIN")
    void testRequestPageExpectedOkStatus() throws Exception{

        User userResponse = UserMock.mock(true);

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
    @WithMockUser(roles = "ADMIN")
    void testRequestPageExpectedNotFoundStatus() throws Exception{
        
        when(userService.findPage(any(Pageable.class))).thenThrow(new NoResultException("No result in page"));

        mockMvc.perform(get("/users/page"))
                .andExpect(status().isNotFound());

        verify(userService).findPage(any(Pageable.class));
    }


    @Test
    @WithMockUser(roles = "USER")
    void testRequestPageExpectedForbiddenStatus() throws Exception{

        when(userService.findPage(any(Pageable.class))).thenThrow(new NoResultException("No result in page"));

        mockMvc.perform(get("/users/page"))
                .andExpect(status().isNotFound());

        verify(userService).findPage(any(Pageable.class));
    }
}
