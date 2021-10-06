package com.uneeddevs.finances.services;


import com.uneeddevs.finances.mocks.BankAccountMock;
import com.uneeddevs.finances.mocks.UserMock;
import com.uneeddevs.finances.model.BankAccount;
import com.uneeddevs.finances.model.User;
import com.uneeddevs.finances.repository.BankAccountRepository;
import com.uneeddevs.finances.service.BankAccountService;
import com.uneeddevs.finances.service.UserService;
import com.uneeddevs.finances.service.impl.BankAccountServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.NoResultException;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BankAccountServiceTest {

    private BankAccountService bankAccountService;
    private BankAccountRepository bankAccountRepository;
    private UserService userService;

    @BeforeEach
    void setup() {
        userService = mock(UserService.class);
        bankAccountRepository = mock(BankAccountRepository.class);
        bankAccountService = new BankAccountServiceImpl(bankAccountRepository, userService);
    }

    @Test
    void testCreateNewAccountExpectedSuccess() throws Exception {
        User userMock = UserMock.mock(false);
        UUID userID = userMock.getId();

        BankAccount bankAccount = new BankAccount(BigDecimal.ZERO, "Bank test", userMock);

        when(userService.findById(userID)).thenReturn(userMock);
        when(bankAccountRepository.save(bankAccount)).thenReturn(bankAccount);

        BankAccount persistBankAccount = bankAccountService.insert(bankAccount);

        assertNotNull(persistBankAccount, "Persist bank account cannot be null");

        verify(userService).findById(userID);
        verify(bankAccountRepository).save(bankAccount);
    }

    @Test
    void testCreateNewAccountWithNonexistentUserExpectedNoResultException() throws Exception {
        User userMock = UserMock.mock(false);
        UUID userID = userMock.getId();

        BankAccount bankAccount = new BankAccount(BigDecimal.ZERO, "Bank test", userMock);

        when(userService.findById(userID)).thenThrow(NoResultException.class);
        when(bankAccountRepository.save(bankAccount)).thenReturn(bankAccount);

        assertThrows(NoResultException.class, () -> bankAccountService.insert(bankAccount));

        verify(userService).findById(userID);
        verify(bankAccountRepository, never()).save(bankAccount);
    }

    @Test
    void testFindByIdExpectedSuccess() throws Exception {
        BankAccount bankAccountMock = BankAccountMock.mock();
        UUID id = UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6");

        when(bankAccountRepository.findById(id)).thenReturn(Optional.of(bankAccountMock));

        BankAccount bankAccount = bankAccountService.findById(id);

        assertNotNull(bankAccount, "Persist bank account cannot be null");

        verify(bankAccountRepository).findById(id);
    }

    @Test
    void testFindByIdExpectedNoResultException() {
        UUID id = UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6");

        when(bankAccountRepository.findById(id)).thenReturn(Optional.empty());

        NoResultException noResultException = assertThrows(NoResultException.class, () -> bankAccountService.findById(id));

        assertMessageNoResultException(noResultException);

        verify(bankAccountRepository).findById(id);
    }

    @Test
    void testDeleteByIdExpectedSuccess() throws Exception {
        BankAccount bankAccountMock = BankAccountMock.mock();
        UUID id = UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6");

        when(bankAccountRepository.findById(id)).thenReturn(Optional.of(bankAccountMock));

        boolean deleted = bankAccountService.deleteById(id);

        assertTrue(deleted, "Expected deleted");

        verify(bankAccountRepository).findById(id);
        verify(bankAccountRepository).delete(bankAccountMock);

    }

    @Test
    void testDeleteByIdExpectedNoResultException() {
        UUID id = UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6");

        when(bankAccountRepository.findById(id)).thenReturn(Optional.empty());

        NoResultException noResultException = assertThrows(NoResultException.class,
                () -> bankAccountService.deleteById(id));

        assertMessageNoResultException(noResultException);

        verify(bankAccountRepository).findById(id);
        verify(bankAccountRepository, never()).delete(any(BankAccount.class));
    }


    @Test
    void testDeleteByIdExpectedError() throws Exception {
        BankAccount bankAccountMock = BankAccountMock.mock();
        UUID id = UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6");

        when(bankAccountRepository.findById(id)).thenReturn(Optional.of(bankAccountMock));
        doThrow(RuntimeException.class).when(bankAccountRepository).delete(bankAccountMock);

        assertThrows(RuntimeException.class, () -> bankAccountService.deleteById(id));

        verify(bankAccountRepository).findById(id);
        verify(bankAccountRepository).delete(bankAccountMock);

    }

    @Test
    void testUpdatedExpectedSuccess() throws Exception {
        BankAccount bankAccountMock = BankAccountMock.mock();
        BankAccount bankAccountUpdate = BankAccountMock.mock();
        bankAccountUpdate.setName("Updated");
        UUID id = UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6");


        when(bankAccountRepository.findById(id)).thenReturn(Optional.of(bankAccountMock));

        bankAccountService.update(bankAccountUpdate);

        assertEquals("Updated", bankAccountMock.getName(), "Expected update name");

        verify(bankAccountRepository).findById(id);
        verify(bankAccountRepository).save(bankAccountMock);
    }

    @Test
    void testUpdatedNoResultException() throws Exception {
        BankAccount bankAccountMock = BankAccountMock.mock();
        BankAccount bankAccountUpdate = BankAccountMock.mock();
        bankAccountUpdate.setName("Updated");
        UUID id = UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6");


        when(bankAccountRepository.findById(id)).thenReturn(Optional.empty());

        NoResultException noResultException = assertThrows(NoResultException.class,
                () ->bankAccountService.update(bankAccountUpdate));
        assertMessageNoResultException(noResultException);
        verify(bankAccountRepository).findById(id);
        verify(bankAccountRepository, never()).save(bankAccountMock);
    }

    private void assertMessageNoResultException(NoResultException noResultException) {
        assertEquals("No bank account with id 3fa85f64-5717-4562-b3fc-2c963f66afa6",
                noResultException.getMessage(),
                "Expected message: No bank account with id 3fa85f64-5717-4562-b3fc-2c963f66afa6");
    }

}
