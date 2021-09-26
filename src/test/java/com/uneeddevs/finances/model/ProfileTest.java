package com.uneeddevs.finances.model;

import com.uneeddevs.finances.mocks.ProfileMock;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProfileTest {

    @Test
    void testProfileCreateInstanceNotNull() {
        Profile profile = new Profile("ROLE_ADMIN");
        assertNotNull(profile, "Expected profile instance");
    }

    @Test
    void testProfileInstanceExpectedIllegalArgumentException() {
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class,
                () -> new Profile(""),
                "Expected illegal argument exception");
        assertEquals(illegalArgumentException.getMessage(),
                "Role Name is mandatory"
                );
    }

    @Test
    void testEqualsAndHashCode() throws Exception{
        Profile profile1 = ProfileMock.mock();
        Profile profile2 = ProfileMock.mock();
        assertEquals(profile2, profile1);
        assertEquals(profile1.hashCode(), profile2.hashCode());
    }

}
