package com.uneeddevs.finances.dto;

import com.uneeddevs.finances.mocks.BankAccountInsertDTOMock;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class BankAccountInsertDTOTest {

    @Test
    void testCreateBankAccountInsertDTOExpectedSuccess() {
        BankAccountInsertDTO bankAccountInsertDTO = new BankAccountInsertDTO();
        assertNotNull(bankAccountInsertDTO, "Bank Account Insert cannot be null");
    }

    @Test
    void testToBankAccountModelExpectedSuccess() throws Exception {
        BankAccountInsertDTO bankAccountInsertDTO = BankAccountInsertDTOMock.mock();
        assertNotNull(bankAccountInsertDTO.toModel(), "Have to be a instance of Bank Account");
    }

}
