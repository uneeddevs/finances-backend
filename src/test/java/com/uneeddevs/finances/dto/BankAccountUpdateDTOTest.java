package com.uneeddevs.finances.dto;

import com.uneeddevs.finances.mocks.BankAccountUpdateDTOMock;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class BankAccountUpdateDTOTest {


    @Test
    void testCreateBankAccountUpdateDTOExpectedSuccess() {
        BankAccountUpdateDTO bankAccountUpdateDTO = new BankAccountUpdateDTO();
        assertNotNull(bankAccountUpdateDTO, "Bank Account update cannot be null");
    }

    @Test
    void testToBankAccountModelExpectedSuccess() throws Exception {
        BankAccountUpdateDTO bankAccountInsertDTO = BankAccountUpdateDTOMock.mock();
        UUID uuid = UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6");
        assertNotNull(bankAccountInsertDTO.toModel(uuid), "Have to be a instance of Bank Account");
    }

}
