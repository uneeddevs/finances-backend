package com.uneeddevs.finances.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class TokenDTOTest {

    @Test
    void testCreateInstanceWithBuilderExpectedSuccess() {
        final TokenDTO token = TokenDTO.builder()
                .expiration(10000000L)
                .token("token")
                .build();
        assertEquals(10000000L, token.getExpiration());
        assertEquals("token", token.getToken());
    }

    @Test
    void testCreateInstanceWithoutBuilderExpectedSuccess() {
        final TokenDTO token = new TokenDTO();
        assertNull(token.getExpiration());
        assertNull(token.getToken());
    }

}
