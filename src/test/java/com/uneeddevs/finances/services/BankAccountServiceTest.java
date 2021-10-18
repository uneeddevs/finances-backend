package com.uneeddevs.finances.services;


import com.uneeddevs.finances.enums.ProfileRole;
import com.uneeddevs.finances.mocks.BankAccountMock;
import com.uneeddevs.finances.mocks.UserMock;
import com.uneeddevs.finances.model.BankAccount;
import com.uneeddevs.finances.model.User;
import com.uneeddevs.finances.repository.BankAccountRepository;
import com.uneeddevs.finances.security.exception.AuthenticationFailException;
import com.uneeddevs.finances.service.BankAccountService;
import com.uneeddevs.finances.service.UserService;
import com.uneeddevs.finances.service.impl.BankAccountServiceImpl;
import com.uneeddevs.finances.util.UserUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import javax.persistence.NoResultException;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
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
        BankAccount bankAccount = new BankAccount(BigDecimal.ZERO, "Bank test", userMock);

        when(bankAccountRepository.save(bankAccount)).thenReturn(bankAccount);

        BankAccount persistBankAccount = bankAccountService.save(bankAccount);

        assertNotNull(persistBankAccount, "Persist bank account cannot be null");
        verify(bankAccountRepository).save(bankAccount);
    }

    @Test
    void testFindByIdExpectedSuccess() throws Exception {
        try (MockedStatic<UserUtil> mockedUserUtil = Mockito.mockStatic(UserUtil.class)) {
            mockedUserUtil.when(() -> UserUtil.hasAuthority(any(ProfileRole.class))).thenReturn(true);
            BankAccount bankAccountMock = BankAccountMock.mock();
            UUID id = UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6");

            when(bankAccountRepository.findById(id)).thenReturn(Optional.of(bankAccountMock));

            BankAccount bankAccount = bankAccountService.findById(id);

            assertNotNull(bankAccount, "Persist bank account cannot be null");
            verify(bankAccountRepository).findById(id);
        }
    }

    @Test
    void testFindByIdExpectedAuthenticationFailException() throws Exception {
        try (MockedStatic<UserUtil> mockedUserUtil = Mockito.mockStatic(UserUtil.class)) {
            mockedUserUtil.when(() -> UserUtil.hasAuthority(any(ProfileRole.class))).thenReturn(false);
            BankAccount bankAccountMock = BankAccountMock.mock();
            UUID id = UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6");

            when(bankAccountRepository.findById(id)).thenReturn(Optional.of(bankAccountMock));

            assertThrows(AuthenticationFailException.class, () -> bankAccountService.findById(id));

            verify(bankAccountRepository).findById(id);
        }
    }

    @Test
    void testFindByIdAuthenticationFailException() {
        try (MockedStatic<UserUtil> mockedUserUtil = Mockito.mockStatic(UserUtil.class)) {
            mockedUserUtil.when(() -> UserUtil.hasAuthority(any(ProfileRole.class))).thenReturn(false);
            UUID id = UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6");

            when(bankAccountRepository.findById(id)).thenReturn(Optional.empty());

            assertThrows(AuthenticationFailException.class, () -> bankAccountService.findById(id));

            verify(bankAccountRepository).findById(id);
        }
    }

    @Test
    void testFindByIdNoResultException() {
        try (MockedStatic<UserUtil> mockedUserUtil = Mockito.mockStatic(UserUtil.class)) {
            mockedUserUtil.when(() -> UserUtil.hasAuthority(any(ProfileRole.class))).thenReturn(true);
            UUID id = UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6");

            when(bankAccountRepository.findById(id)).thenReturn(Optional.empty());

            assertThrows(NoResultException.class, () -> bankAccountService.findById(id));

            verify(bankAccountRepository).findById(id);
        }
    }

    @Test
    void testDeleteByIdExpectedSuccess() throws Exception {
        try (MockedStatic<UserUtil> mockedUserUtil = Mockito.mockStatic(UserUtil.class)) {
            mockedUserUtil.when(() -> UserUtil.hasAuthority(any(ProfileRole.class))).thenReturn(true);
            BankAccount bankAccountMock = BankAccountMock.mock();
            UUID id = UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6");

            when(bankAccountRepository.findById(id)).thenReturn(Optional.of(bankAccountMock));

            boolean deleted = bankAccountService.deleteById(id);

            assertTrue(deleted, "Expected deleted");

            verify(bankAccountRepository).findById(id);
            verify(bankAccountRepository).delete(bankAccountMock);
        }

    }

    @Test
    void testDeleteByIdExpectedNoResultException() {
        try (MockedStatic<UserUtil> mockedUserUtil = Mockito.mockStatic(UserUtil.class)) {
            mockedUserUtil.when(() -> UserUtil.hasAuthority(any(ProfileRole.class))).thenReturn(true);
            UUID id = UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6");

            when(bankAccountRepository.findById(id)).thenReturn(Optional.empty());

           assertThrows(NoResultException.class,
                    () -> bankAccountService.deleteById(id));


            verify(bankAccountRepository).findById(id);
            verify(bankAccountRepository, never()).delete(any(BankAccount.class));
        }
    }


    @Test
    void testDeleteByIdExpectedAuthenticationFailException() {
        try (MockedStatic<UserUtil> mockedUserUtil = Mockito.mockStatic(UserUtil.class)) {
            mockedUserUtil.when(() -> UserUtil.hasAuthority(any(ProfileRole.class))).thenReturn(false);
            UUID id = UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6");

            when(bankAccountRepository.findById(id)).thenReturn(Optional.empty());

            assertThrows(AuthenticationFailException.class,
                    () -> bankAccountService.deleteById(id));


            verify(bankAccountRepository).findById(id);
            verify(bankAccountRepository, never()).delete(any(BankAccount.class));
        }
    }




    @Test
    void testDeleteByIdExpectedError() throws Exception {
        try (MockedStatic<UserUtil> mockedUserUtil = Mockito.mockStatic(UserUtil.class)) {
            mockedUserUtil.when(() -> UserUtil.hasAuthority(any(ProfileRole.class))).thenReturn(true);
            BankAccount bankAccountMock = BankAccountMock.mock();
            UUID id = UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6");

            when(bankAccountRepository.findById(id)).thenReturn(Optional.of(bankAccountMock));
            doThrow(RuntimeException.class).when(bankAccountRepository).delete(bankAccountMock);

            assertThrows(RuntimeException.class, () -> bankAccountService.deleteById(id));

            verify(bankAccountRepository).findById(id);
            verify(bankAccountRepository).delete(bankAccountMock);
        }

    }

    @Test
    void testUpdatedExpectedSuccess() throws Exception {
        try (MockedStatic<UserUtil> mockedUserUtil = Mockito.mockStatic(UserUtil.class)) {
            mockedUserUtil.when(() -> UserUtil.hasAuthority(any(ProfileRole.class))).thenReturn(true);
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
    }

    @Test
    void testUpdatedWithExistentAccountAuthenticationFailException() throws Exception {
        try (MockedStatic<UserUtil> mockedUserUtil = Mockito.mockStatic(UserUtil.class)) {
            mockedUserUtil.when(() -> UserUtil.hasAuthority(any(ProfileRole.class))).thenReturn(false);
            BankAccount bankAccountMock = BankAccountMock.mock();
            BankAccount bankAccountUpdate = BankAccountMock.mock();
            bankAccountUpdate.setName("Updated");
            UUID id = UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6");


            when(bankAccountRepository.findById(id)).thenReturn(Optional.of(bankAccountMock));

            assertThrows(AuthenticationFailException.class , () ->bankAccountService.update(bankAccountUpdate));


            verify(bankAccountRepository).findById(id);
            verify(bankAccountRepository, never()).save(bankAccountMock);
        }
    }

    @Test
    void testUpdatedNoResultException() throws Exception {
        try (MockedStatic<UserUtil> mockedUserUtil = Mockito.mockStatic(UserUtil.class)) {
            mockedUserUtil.when(() -> UserUtil.hasAuthority(any(ProfileRole.class))).thenReturn(true);
            mockedUserUtil.when(UserUtil::authenticatedUUID)
                    .thenReturn(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa7"));
            BankAccount bankAccountMock = BankAccountMock.mock();
            BankAccount bankAccountUpdate = BankAccountMock.mock();
            bankAccountUpdate.setName("Updated");
            UUID id = UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6");


            when(bankAccountRepository.findById(id)).thenReturn(Optional.empty());

            assertThrows(NoResultException.class,
                    () -> bankAccountService.update(bankAccountUpdate));
            verify(bankAccountRepository).findById(id);
            verify(bankAccountRepository, never()).save(bankAccountMock);
        }
    }

    @Test
    void testUpdatedAuthenticationFailException() throws Exception {
        try (MockedStatic<UserUtil> mockedUserUtil = Mockito.mockStatic(UserUtil.class)) {
            mockedUserUtil.when(() -> UserUtil.hasAuthority(any(ProfileRole.class))).thenReturn(false);
            mockedUserUtil.when(UserUtil::authenticatedUUID)
                    .thenReturn(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa7"));
            BankAccount bankAccountMock = BankAccountMock.mock();
            BankAccount bankAccountUpdate = BankAccountMock.mock();
            bankAccountUpdate.setName("Updated");
            UUID id = UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6");


            when(bankAccountRepository.findById(id)).thenReturn(Optional.empty());

            assertThrows(AuthenticationFailException.class,
                    () -> bankAccountService.update(bankAccountUpdate));
            verify(bankAccountRepository).findById(id);
            verify(bankAccountRepository, never()).save(bankAccountMock);
        }
    }

    @Test
    void testFindByUserExpectedSuccess() throws Exception {
        try (MockedStatic<UserUtil> mockedUserUtil = Mockito.mockStatic(UserUtil.class)) {
            mockedUserUtil.when(() -> UserUtil.hasAuthority(any(ProfileRole.class))).thenReturn(true);
            BankAccount bankAccount = BankAccountMock.mock();
            List<BankAccount> bankAccountList = Collections.singletonList(bankAccount);
            User user = UserMock.mock(false);
            UUID uuid = UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6");

            when(userService.findById(uuid)).thenReturn(user);
            when(bankAccountRepository.findByUser(user)).thenReturn(bankAccountList);

            List<BankAccount> methodResponse = bankAccountService.findByUser(user);

            assertFalse(methodResponse.isEmpty(), "Bank account list cannot be empty");

            verify(userService).findById(uuid);
            verify(bankAccountRepository).findByUser(user);
        }

    }

    @Test
    void testFindByUserWithNonExistentUserExpectedNoResultException() throws Exception {
        try (MockedStatic<UserUtil> mockedUserUtil = Mockito.mockStatic(UserUtil.class)) {
            mockedUserUtil.when(() -> UserUtil.hasAuthority(any(ProfileRole.class))).thenReturn(true);
            User user = UserMock.mock(false);
            UUID uuid = UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6");

            when(userService.findById(uuid)).thenThrow(new NoResultException("No user"));

            assertThrows(NoResultException.class, () -> bankAccountService.findByUser(user));

            verify(userService).findById(uuid);
            verify(bankAccountRepository, never()).findByUser(user);
        }
    }

    @Test
    void testFindByUserExpectedNoResultException() throws Exception {
        try (MockedStatic<UserUtil> mockedUserUtil = Mockito.mockStatic(UserUtil.class)) {
            mockedUserUtil.when(() -> UserUtil.hasAuthority(any(ProfileRole.class))).thenReturn(true);
            User user = UserMock.mock(false);
            UUID uuid = UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6");

            when(userService.findById(uuid)).thenReturn(user);
            when(bankAccountRepository.findByUser(user)).thenReturn(Collections.emptyList());

            assertThrows(NoResultException.class, () -> bankAccountService.findByUser(user));

            verify(userService).findById(uuid);
            verify(bankAccountRepository).findByUser(user);
        }
    }

    @Test
    void testFindByUserExpectedAuthenticationFailException() throws Exception {
        try (MockedStatic<UserUtil> mockedUserUtil = Mockito.mockStatic(UserUtil.class)) {
            mockedUserUtil.when(() -> UserUtil.hasAuthority(any(ProfileRole.class))).thenReturn(false);
            User user = UserMock.mock(false);
            UUID uuid = UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6");

            when(userService.findById(uuid)).thenReturn(user);
            when(bankAccountRepository.findByUser(user)).thenReturn(Collections.emptyList());

            assertThrows(AuthenticationFailException.class, () -> bankAccountService.findByUser(user));

            verify(userService, never()).findById(uuid);
            verify(bankAccountRepository, never()).findByUser(user);
        }
    }

}
