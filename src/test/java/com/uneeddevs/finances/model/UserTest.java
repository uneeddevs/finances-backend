package com.uneeddevs.finances.model;

import com.uneeddevs.finances.mocks.UserMock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

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
        User user = UserMock.mock();
        assertNotNull(user.toUserResponseDTO(), "Have to be a instance of UserResponseDTO");
    }

    @Test
    void testAreEquals() throws Exception {
        User user1 = UserMock.mock();
        User user2 = UserMock.mock();
        assertEquals(user1.hashCode(), user2.hashCode());
        assertEquals(user1, user2);
    }

}
