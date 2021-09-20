package com.uneeddevs.finances.dto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class UserResponseDTOTest {

    @Test
    void testCreateUserExpectedSuccess() {
        UserResponseDTO user = new UserResponseDTO(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"),
                "name",
                "email@mail.com",
                LocalDateTime.now());
        assertNotNull(user, "User cannot be null");
    }

    @ParameterizedTest
    @MethodSource(value =  "testCreationParams")
    void testCreateUserWithoutAnyArgIllegalArgumentException(String uuid, String name, String email, LocalDateTime registerDate, String fieldName) {
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () ->
                new UserResponseDTO(UUID.fromString(uuid),
                name, email, registerDate),
                "Expected illegal Argument Exception");
        String nameIsMandatory = fieldName + " is mandatory";
        assertEquals(nameIsMandatory,
                illegalArgumentException.getMessage(),
                "Expected message: " + nameIsMandatory);
    }

    private static Stream<Arguments> testCreationParams() {
        return Stream.of(
                Arguments.of("3fa85f64-5717-4562-b3fc-2c963f66afa6", null, "user@mail.com", LocalDateTime.now(), "Name"),
                Arguments.of("3fa85f64-5717-4562-b3fc-2c963f66afa6", "name", null, LocalDateTime.now(), "Email"),
                Arguments.of("3fa85f64-5717-4562-b3fc-2c963f66afa6", "name", "user@mail.com", null, "Register date"));
    }


}
