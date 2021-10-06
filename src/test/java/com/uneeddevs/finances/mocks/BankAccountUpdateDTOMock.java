package com.uneeddevs.finances.mocks;

import com.uneeddevs.finances.dto.BankAccountUpdateDTO;

import java.lang.reflect.Field;

public class BankAccountUpdateDTOMock {

    private BankAccountUpdateDTOMock(){}

    public static BankAccountUpdateDTO mock()  throws Exception {
        return mock("Account");
    }

    public static  BankAccountUpdateDTO mock(String accountName) throws Exception {
        BankAccountUpdateDTO bankAccountUpdateDTO = new BankAccountUpdateDTO();
        Class<BankAccountUpdateDTO> clazz = BankAccountUpdateDTO.class;
        Field nameField = clazz.getDeclaredField("accountName");
        nameField.setAccessible(true);
        nameField.set(bankAccountUpdateDTO, accountName);
        return bankAccountUpdateDTO;
    }
}
