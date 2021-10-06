package com.uneeddevs.finances.mocks;

import com.uneeddevs.finances.dto.BankAccountInsertDTO;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.UUID;

public class BankAccountInsertDTOMock {

    private static final UUID uuid = UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6");

    private BankAccountInsertDTOMock(){}

    public static BankAccountInsertDTO mock() throws Exception {
        return mock("Account", uuid, BigDecimal.valueOf(100d));
    }

    public static BankAccountInsertDTO mock(String name, UUID userId, BigDecimal initialBalance) throws Exception{
        BankAccountInsertDTO bankAccountInsertDTO = new BankAccountInsertDTO();
        Class<BankAccountInsertDTO> clazz = BankAccountInsertDTO.class;
        Field nameField = clazz.getDeclaredField("name");
        nameField.setAccessible(true);
        Field userIdField = clazz.getDeclaredField("userId");
        userIdField.setAccessible(true);
        Field initialBalanceField = clazz.getDeclaredField("initialBalance");
        initialBalanceField.setAccessible(true);
        nameField.set(bankAccountInsertDTO, name);
        userIdField.set(bankAccountInsertDTO, userId);
        initialBalanceField.set(bankAccountInsertDTO, initialBalance);
        return bankAccountInsertDTO;
    }
}
