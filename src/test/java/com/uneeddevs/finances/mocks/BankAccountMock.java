package com.uneeddevs.finances.mocks;

import com.uneeddevs.finances.model.BankAccount;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.UUID;

public class BankAccountMock {

    private BankAccountMock(){}

    private static final UUID uuid = UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6");

    public static BankAccount mock() throws Exception {
        BankAccount bankAccount = new BankAccount(BigDecimal.ZERO, "Account 1", UserMock.mock(false));
        Field field = BankAccount.class.getDeclaredField("id");
        field.setAccessible(true);
        field.set(bankAccount, uuid);
        return bankAccount;
    }

}
