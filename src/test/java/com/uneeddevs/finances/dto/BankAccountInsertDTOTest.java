package com.uneeddevs.finances.dto;

import com.uneeddevs.finances.mocks.BankAccountInsertDTOMock;
import com.uneeddevs.finances.mocks.UserMock;
import com.uneeddevs.finances.util.UserUtil;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class BankAccountInsertDTOTest {

    @Test
    void testCreateBankAccountInsertDTOExpectedSuccess() {
        BankAccountInsertDTO bankAccountInsertDTO = new BankAccountInsertDTO();
        assertNotNull(bankAccountInsertDTO, "Bank Account Insert cannot be null");
    }

    @Test
    void testToBankAccountModelExpectedSuccess() throws Exception {
        try (MockedStatic<UserUtil> mockedUserUtil = Mockito.mockStatic(UserUtil.class)) {
            mockedUserUtil.when(UserUtil::authenticated).thenReturn(UserMock.mock(false));
            BankAccountInsertDTO bankAccountInsertDTO = BankAccountInsertDTOMock.mock();
            assertNotNull(bankAccountInsertDTO.toModel(), "Have to be a instance of Bank Account");
        }
    }

}
