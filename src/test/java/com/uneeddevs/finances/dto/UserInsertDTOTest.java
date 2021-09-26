package com.uneeddevs.finances.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class UserInsertDTOTest {

    @Test
    void testCreateUserExpectedSuccess() {
        UserInsertDTO user = new UserInsertDTO("name", "email@mail.com", "password");
        assertNotNull(user, "User cannot be null");
    }

    @Test
    void testToUserModelExpectedSuccess() throws Exception {
        UserInsertDTO user = new UserInsertDTO("name", "email@mail.com", "password");
        assertNotNull(user.toModel(), "Have to be a instance of UserModel");
    }

}
