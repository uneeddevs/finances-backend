package com.uneeddevs.finances.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class CredentialsDTOTest {


    @Test
    void testCreateInstanceWithBuilderExpectedSuccess() {
        final CredentialsDTO credentials = CredentialsDTO.builder()
                .email("user@mail.com")
                .password("password")
                .build();
        assertEquals("user@mail.com", credentials.getEmail());
        assertEquals("password", credentials.getPassword());
    }

    @Test
    void testCreateInstanceWithoutBuilderExpectedSuccess() {
        final CredentialsDTO credentials = new CredentialsDTO();
        assertNull(credentials.getEmail());
        assertNull(credentials.getPassword());
    }

}
