package com.uneeddevs.finances.model;

import com.uneeddevs.finances.dto.UserResponseDTO;
import com.uneeddevs.finances.mocks.ProfileMock;
import com.uneeddevs.finances.mocks.UserMock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

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
        User user = UserMock.mock(true);
        UserResponseDTO userDto = user.toUserResponseDTO();
        assertNotNull(userDto, "Have to be a instance of UserResponseDTO");
        assertFalse(userDto.getProfiles().isEmpty(), "Instance cannot be empty profiles");
    }

    @Test
    void testAreEquals() throws Exception {
        User user1 = UserMock.mock(false);
        User user2 = UserMock.mock(false);
        assertEquals(user1.hashCode(), user2.hashCode());
        assertEquals(user1, user2);
    }

    @Test
    void testAddProfileExpectedSuccess() throws Exception {
        Profile profile = ProfileMock.mock();
        User user = UserMock.mock(false);
        assertDoesNotThrow(() -> user.addProfile(profile), "Cannot throws nothing");
    }


    @Test
    void testAddProfileExpectedIllegalArgumentException() throws Exception {
        User user = UserMock.mock(false);
        assertThrows(IllegalArgumentException.class,
                () -> user.addProfile(null),
                "Expected throws IllegalArgumentException");
    }

    @Test
    void testGetBankAccountsExpectedNotEmpty() throws Exception {
        User user = UserMock.mock(false, true);
        assertFalse(user.getBankAccounts().isEmpty());
    }

    @Test
    void testGetBankAccountsExpectedEmpty() throws Exception {
        User user = UserMock.mock(false, false);
        assertTrue(user.getBankAccounts().isEmpty());
    }

    @ParameterizedTest
    @MethodSource(value = "accountSecurityMethodSource")
    void testSecurityAccountAllTrueStatus(boolean status) {
        assertTrue(status, "Account status have to be true");
    }

    private static Stream<Arguments> accountSecurityMethodSource() throws Exception {
        User user = UserMock.mock(false);
        return Stream.of(
                Arguments.of(user.isAccountNonExpired()),
                Arguments.of(user.isAccountNonLocked()),
                Arguments.of(user.isEnabled()),
                Arguments.of(user.isCredentialsNonExpired())
        );
    }


}
