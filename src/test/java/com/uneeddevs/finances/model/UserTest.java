package com.uneeddevs.finances.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testCreateUserExpectedSuccess() {
        User user = new User("name", "email@mail.com", "password");
        assertNotNull(user, "User cannot be null");
    }

    @ParameterizedTest
    @MethodSource(value =  "testCreationParams")
    void testCreateUserWithoutAnyArgIllegalArgumentException(String name, String email, String password, String fieldBlank) {
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () ->
                new User(name, email, password),
                "Expected illegal Argument Exception");
        String nameIsMandatory = fieldBlank + " is mandatory";
        assertEquals(nameIsMandatory,
                illegalArgumentException.getMessage(),
                "Expected message: " + nameIsMandatory);
    }

    private static Stream<Arguments> testCreationParams() {
        return Stream.of(Arguments.of(null, "user@mail.com", "password", "Name"),
                Arguments.of("name", null, "password", "Email"),
                Arguments.of("name", "user@mail.com", null, "Password"));
    }

    @Test
    void testToUserResponseDTOExpectedSuccess() throws Exception {
        User user = new User("name", "email@mail.com", "password");
        final Class<User> userClass = User.class;
        Field idField = userClass.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(user, UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"));
        Field registerDateField = userClass.getDeclaredField("registerDate");
        registerDateField.setAccessible(true);
        registerDateField.set(user, LocalDateTime.now());
        assertNotNull(user.toUserResponseDTO(), "Have to be a instance of UserResponseDTO");
    }

}
