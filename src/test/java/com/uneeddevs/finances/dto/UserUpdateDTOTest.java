package com.uneeddevs.finances.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class UserUpdateDTOTest {

    @Test
    void testCreateUserExpectedSuccess() {
        UserUpdateDTO user = new UserUpdateDTO("name", "password");
        assertNotNull(user, "User cannot be null");
    }

    @Test
    void testToUserModelExpectedSuccess() throws Exception {
        UserUpdateDTO user = new UserUpdateDTO("name", "password");
        assertNotNull(user.toModel("3fa85f64-5717-4562-b3fc-2c963f66afa6"),
                "Have to be a instance of UserModel");
    }

}
