package com.uneeddevs.finances.mocks;

import com.uneeddevs.finances.model.User;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.UUID;

public class UserMock {

    private UserMock(){}

    private static final UUID uuid = UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6");

    public static User mock(boolean withProfile) throws Exception {
        return mock(withProfile, false);
    }

    public static User mock(boolean withProfile, boolean withBankAccount) throws Exception {
        User user = new User("name", "email@mail.com", "password");
        final Class<User> userClass = User.class;
        Field idField = userClass.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(user, uuid);
        Field registerDateField = userClass.getDeclaredField("registerDate");
        registerDateField.setAccessible(true);
        registerDateField.set(user, LocalDateTime.now());
        if(withProfile)
            user.addProfile(ProfileMock.mock());
        if(withBankAccount)
            user.addBankAccount(BankAccountMock.mock());
        return user;
    }
}
