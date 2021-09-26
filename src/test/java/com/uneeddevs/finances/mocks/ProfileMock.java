package com.uneeddevs.finances.mocks;

import com.uneeddevs.finances.model.Profile;

import java.lang.reflect.Field;

public class ProfileMock {

    private static final Long id = 1L;

    private ProfileMock() {}

    public static Profile mock() throws Exception {
        Profile profile = new Profile("ROLE_ADMIN");
        Field field = Profile.class.getDeclaredField("id");
        field.setAccessible(true);
        field.set(profile, id);
        return profile;
    }

}
