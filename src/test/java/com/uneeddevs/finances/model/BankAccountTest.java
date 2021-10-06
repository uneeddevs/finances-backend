package com.uneeddevs.finances.model;

import com.uneeddevs.finances.dto.BankAccountResponseDTO;
import com.uneeddevs.finances.mocks.BankAccountMock;
import com.uneeddevs.finances.mocks.UserMock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class BankAccountTest {

    @Test
    void testBankAccountCreateInstanceNotNull() throws Exception {
        BankAccount bankAccount = new BankAccount(BigDecimal.ZERO,
                "Account test instance",
                UserMock.mock(false));
        assertNotNull(bankAccount, "Account cannot be null");
    }

    @ParameterizedTest
    @MethodSource(value = "illegalArgumentBankAccountProvider")
    void testBankAccountInstanceExpectedIllegalArgumentException(BigDecimal balance,
                                                                 String name,
                                                                 User user) {
        assertThrows(IllegalArgumentException.class, () -> new BankAccount(balance, name, user));
    }

    @Test
    void testBankAccountAddBalanceExpectedSuccess() throws Exception {
        BankAccount bankAccount = BankAccountMock.mock();
        assertDoesNotThrow(() -> bankAccount.addBalance(BigDecimal.valueOf(20.0)));
    }

    @Test
    void testBankAccountAddBalanceExpectedIllegalArgumentException() throws Exception {
        BankAccount bankAccount = BankAccountMock.mock();
        BigDecimal value = BigDecimal.valueOf(-20.0);
        assertThrows(IllegalArgumentException.class, () -> bankAccount.addBalance(value));
    }

    @Test
    void testBankAccountSubtractBalanceExpectedSuccess() throws Exception {
        BankAccount bankAccount = BankAccountMock.mock();
        BigDecimal value = BigDecimal.valueOf(20.0);
        bankAccount.addBalance(value);
        assertDoesNotThrow(() -> bankAccount.subtractBalance(value));
    }

    @Test
    void testBankAccountSubtractBalanceWithNegativeValueExpectedIllegalArgumentException() throws Exception {
        BankAccount bankAccount = BankAccountMock.mock();
        BigDecimal value = BigDecimal.valueOf(-1d);
        assertThrows(IllegalArgumentException.class, () -> bankAccount.subtractBalance(value));
    }

    @Test
    void testBankAccountSubtractBalanceWithGreaterValueThanBalanceValueExpectedIllegalArgumentException() throws Exception {
        BankAccount bankAccount = BankAccountMock.mock();
        BigDecimal value = BigDecimal.valueOf(10d);
        assertThrows(IllegalArgumentException.class, () -> bankAccount.subtractBalance(value));
    }

    @Test
    void testEqualsAndHashCode() throws Exception{
        BankAccount bankAccount1 = BankAccountMock.mock();
        BankAccount bankAccount2 = BankAccountMock.mock();
        assertEquals(bankAccount1, bankAccount2, "Accounts have to be equals");
        assertEquals(bankAccount1.hashCode(), bankAccount2.hashCode());
    }

    @Test
    void testEqualsAndHashCodeFail() throws  Exception {
        BankAccount bankAccount = BankAccountMock.mock();
        assertNotEquals(null, bankAccount);
    }

    @Test
    void testEqualsAndHashCodeFail2() throws  Exception {
        BankAccount bankAccount = BankAccountMock.mock();
        assertEquals(bankAccount, bankAccount);
    }

    @Test
    void testNoNameIllegalArgumentException() {
        UUID uuid = UUID.randomUUID();
        assertThrows(IllegalArgumentException.class, () -> new BankAccount(uuid, ""));
    }

    @Test
    void toResponseDTO() throws Exception {
        BankAccount bankAccount = BankAccountMock.mock();
        BankAccountResponseDTO response = assertDoesNotThrow(bankAccount::toBankAccountResponseDTO, "Cannot throws nothing");
        assertNotNull(response, "Bank account response cannot be null");
    }

    private static Stream<Arguments> illegalArgumentBankAccountProvider() throws Exception{
        return Stream.of(
                Arguments.of(BigDecimal.valueOf(-1d), "Account", UserMock.mock(false)),
                Arguments.of(BigDecimal.valueOf(10d), "", UserMock.mock(false))
        );
    }


}
