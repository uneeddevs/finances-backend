package com.uneeddevs.finances.dto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class UserResponseDTOTest {

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
    void testCreateUserWithoutAnyArgIllegalArgumentException(UUID uuid, String name, String email, LocalDateTime registerDate, String fieldName) {
        assertThrows(IllegalArgumentException.class, () ->
                new UserResponseDTO(uuid,
                name, email, registerDate),
                "Expected illegal Argument Exception");
    }

    private static Stream<Arguments> testCreationParams() {
        return Stream.of(
                Arguments.of(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"), null, "user@mail.com", LocalDateTime.now(), "Name"),
                Arguments.of(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6") , "name", null, LocalDateTime.now(), "Email"));
    }


}
