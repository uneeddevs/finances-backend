package com.uneeddevs.finances.mocks;

import com.uneeddevs.finances.dto.BankAccountInsertDTO;

import java.lang.reflect.Field;
import java.math.BigDecimal;

public class BankAccountInsertDTOMock {

    private BankAccountInsertDTOMock(){}

    public static BankAccountInsertDTO mock() throws Exception {
        return mock("Account", BigDecimal.valueOf(100d));
    }

    public static BankAccountInsertDTO mock(String name,  BigDecimal initialBalance) throws Exception{
        BankAccountInsertDTO bankAccountInsertDTO = new BankAccountInsertDTO();
        Class<BankAccountInsertDTO> clazz = BankAccountInsertDTO.class;
        Field nameField = clazz.getDeclaredField("name");
        nameField.setAccessible(true);
        Field initialBalanceField = clazz.getDeclaredField("initialBalance");
        initialBalanceField.setAccessible(true);
        nameField.set(bankAccountInsertDTO, name);
        initialBalanceField.set(bankAccountInsertDTO, initialBalance);
        return bankAccountInsertDTO;
    }
}
