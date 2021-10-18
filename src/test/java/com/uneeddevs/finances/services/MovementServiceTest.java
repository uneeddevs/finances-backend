package com.uneeddevs.finances.services;

import com.uneeddevs.finances.enums.ProfileRole;
import com.uneeddevs.finances.mocks.BankAccountMock;
import com.uneeddevs.finances.mocks.MovementMock;
import com.uneeddevs.finances.model.BankAccount;
import com.uneeddevs.finances.model.Movement;
import com.uneeddevs.finances.repository.MovementRepository;
import com.uneeddevs.finances.security.exception.AuthenticationFailException;
import com.uneeddevs.finances.service.BankAccountService;
import com.uneeddevs.finances.service.MovementService;
import com.uneeddevs.finances.service.impl.MovementServiceImpl;
import com.uneeddevs.finances.util.UserUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import javax.persistence.NoResultException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MovementServiceTest {

    private MovementService movementService;
    private MovementRepository movementRepository;
    private BankAccountService bankAccountService;

    @BeforeEach
    void setup() {
        movementRepository = mock(MovementRepository.class);
        bankAccountService = mock(BankAccountService.class);
        movementService = new MovementServiceImpl(movementRepository, bankAccountService);
    }

    @Test
    void testInsertNewInputMovementExpectedSuccess() throws Exception {

        BankAccount bankAccount = BankAccountMock.mock();
        Movement movement = MovementMock.mock(true);
        UUID uuid = UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6");

        when(bankAccountService.findById(uuid)).thenReturn(bankAccount);
        when(movementRepository.save(movement)).thenReturn(movement);
        when(bankAccountService.save(bankAccount)).thenReturn(bankAccount);

        movementService.save(movement);

        verify(bankAccountService).findById(uuid);
        verify(movementRepository).save(movement);
        verify(bankAccountService).save(bankAccount);

        assertEquals(BigDecimal.TEN, bankAccount.getBalance(), "Balance has to be 10.0");

    }

    @Test
    void testInsertNewOutputMovementExpectedSuccess() throws Exception {

        BankAccount bankAccount = BankAccountMock.mock();
        bankAccount.addBalance(BigDecimal.TEN);
        Movement movement = MovementMock.mock(false);
        UUID uuid = UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6");

        when(bankAccountService.findById(uuid)).thenReturn(bankAccount);
        when(movementRepository.save(movement)).thenReturn(movement);
        when(bankAccountService.save(bankAccount)).thenReturn(bankAccount);

        movementService.save(movement);

        verify(bankAccountService).findById(uuid);
        verify(movementRepository).save(movement);
        verify(bankAccountService).save(bankAccount);

        assertEquals(BigDecimal.ZERO, bankAccount.getBalance(), "Balance has to be 0.0");

    }

    @Test
    void testInsertNewOutputMovementExpectedIllegalArgumentException() throws Exception {

        BankAccount bankAccount = BankAccountMock.mock();
        Movement movement = MovementMock.mock(false);
        UUID uuid = UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6");

        when(bankAccountService.findById(uuid)).thenReturn(bankAccount);

        assertThrows(IllegalArgumentException.class, () -> movementService.save(movement),
                "Expected throws IllegalArgumentException");

        verify(bankAccountService).findById(uuid);
        verify(movementRepository, never()).save(movement);
        verify(bankAccountService, never()).save(bankAccount);

    }

    @Test
    void testDeleteInputMovementByIdExpectedSuccess() throws Exception {
        try(MockedStatic<UserUtil> mockedUserUtil = Mockito.mockStatic(UserUtil.class)) {
            mockedUserUtil.when(() -> UserUtil.hasAuthority(any(ProfileRole.class))).thenReturn(true);
            Movement movement = MovementMock.mock(true);
            BankAccount bankAccount = movement.getBankAccount();
            bankAccount.addBalance(BigDecimal.valueOf(20.0));
            UUID uuid = UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6");

            when(movementRepository.findById(uuid)).thenReturn(Optional.of(movement));
            when(bankAccountService.save(bankAccount)).thenReturn(bankAccount);
            doNothing().when(movementRepository).delete(movement);

            movementService.deleteMovementById(uuid);

            assertEquals(BigDecimal.valueOf(10.0), bankAccount.getBalance(), "Balance has to be 10.0");

            verify(movementRepository).findById(uuid);
            verify(bankAccountService).save(bankAccount);
            verify(movementRepository).delete(movement);
        }

    }

    @Test
    void testDeleteMovementByIdExpectedAuthenticationFailException() throws Exception {
        try(MockedStatic<UserUtil> mockedUserUtil = Mockito.mockStatic(UserUtil.class)) {
            mockedUserUtil.when(() -> UserUtil.hasAuthority(any(ProfileRole.class))).thenReturn(false);
            Movement movement = MovementMock.mock(true);
            BankAccount bankAccount = movement.getBankAccount();
            bankAccount.addBalance(BigDecimal.valueOf(20.0));
            UUID uuid = UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6");

            when(movementRepository.findById(uuid)).thenReturn(Optional.of(movement));
            when(bankAccountService.save(bankAccount)).thenReturn(bankAccount);
            doNothing().when(movementRepository).delete(movement);

            assertThrows(AuthenticationFailException.class, () -> movementService.deleteMovementById(uuid));


            verify(movementRepository).findById(uuid);
            verify(bankAccountService, never()).save(bankAccount);
            verify(movementRepository, never()).delete(movement);
        }

    }

    @Test
    void testDeleteOutputMovementByIdExpectedSuccess() throws Exception {
        try(MockedStatic<UserUtil> mockedUserUtil = Mockito.mockStatic(UserUtil.class)) {
            mockedUserUtil.when(() -> UserUtil.hasAuthority(any(ProfileRole.class))).thenReturn(true);
            Movement movement = MovementMock.mock(false);
            BankAccount bankAccount = movement.getBankAccount();
            bankAccount.addBalance(BigDecimal.valueOf(20.0));
            UUID uuid = UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6");


            when(movementRepository.findById(uuid)).thenReturn(Optional.of(movement));
            when(bankAccountService.save(bankAccount)).thenReturn(bankAccount);
            doNothing().when(movementRepository).delete(movement);

            movementService.deleteMovementById(uuid);

            assertEquals(BigDecimal.valueOf(30.0), bankAccount.getBalance(), "Balance has to be 30.0");

            verify(movementRepository).findById(uuid);
            verify(bankAccountService).save(bankAccount);
            verify(movementRepository).delete(movement);
        }

    }

    @Test
    void testDeleteInputMovementByIdExpectedIllegalArgumentException() throws Exception {
        try(MockedStatic<UserUtil> mockedUserUtil = Mockito.mockStatic(UserUtil.class)) {
            mockedUserUtil.when(() -> UserUtil.hasAuthority(any(ProfileRole.class))).thenReturn(true);
            Movement movement = MovementMock.mock(true);
            BankAccount bankAccount = movement.getBankAccount();
            UUID uuid = UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6");

            when(movementRepository.findById(uuid)).thenReturn(Optional.of(movement));

            assertThrows(IllegalArgumentException.class, () -> movementService.deleteMovementById(uuid));

            assertEquals(BigDecimal.ZERO, bankAccount.getBalance(), "Balance has to be 0.0");

            verify(movementRepository).findById(uuid);
            verify(bankAccountService, never()).save(bankAccount);
            verify(movementRepository, never()).delete(movement);
        }
    }

    @Test
    void testFindByIdExpectedSuccess() throws Exception {
        try(MockedStatic<UserUtil> mockedUserUtil = Mockito.mockStatic(UserUtil.class)) {
            mockedUserUtil.when(() -> UserUtil.hasAuthority(any(ProfileRole.class))).thenReturn(true);
            UUID uuid = UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6");
            Movement movement = MovementMock.mock(false);

            when(movementRepository.findById(uuid)).thenReturn(Optional.of(movement));
            assertDoesNotThrow(() -> movementService.findById(uuid));

            verify(movementRepository).findById(uuid);
        }

    }

    @Test
    void testFindByIdExpectedAuthenticationFailException() throws Exception {
        try(MockedStatic<UserUtil> mockedUserUtil = Mockito.mockStatic(UserUtil.class)) {
            mockedUserUtil.when(() -> UserUtil.hasAuthority(any(ProfileRole.class))).thenReturn(false);
            UUID uuid = UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6");
            Movement movement = MovementMock.mock(false);

            when(movementRepository.findById(uuid)).thenReturn(Optional.of(movement));
            assertThrows(AuthenticationFailException.class, () -> movementService.findById(uuid));

            verify(movementRepository).findById(uuid);
        }

    }

    @Test
    void testFindByIdExpectedNoResultException() throws Exception {
        try(MockedStatic<UserUtil> mockedUserUtil = Mockito.mockStatic(UserUtil.class)) {
            mockedUserUtil.when(() -> UserUtil.hasAuthority(any(ProfileRole.class))).thenReturn(true);
            UUID uuid = UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6");

            when(movementRepository.findById(uuid)).thenReturn(Optional.empty());
            assertThrows(NoResultException.class, () -> movementService.findById(uuid));

            verify(movementRepository).findById(uuid);
        }

    }

    @Test
    void testFindByIdUnexistentMovementExpectedAuthenticationFailException() throws Exception {
        try(MockedStatic<UserUtil> mockedUserUtil = Mockito.mockStatic(UserUtil.class)) {
            mockedUserUtil.when(() -> UserUtil.hasAuthority(any(ProfileRole.class))).thenReturn(false);
            UUID uuid = UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6");

            when(movementRepository.findById(uuid)).thenReturn(Optional.empty());
            assertThrows(AuthenticationFailException.class, () -> movementService.findById(uuid));

            verify(movementRepository).findById(uuid);
        }

    }

    @Test
    void testFindByPeriodExpectedSuccess() throws Exception{
        try(MockedStatic<UserUtil> mockedUserUtil = Mockito.mockStatic(UserUtil.class)) {
            mockedUserUtil.when(() -> UserUtil.hasAuthority(any(ProfileRole.class))).thenReturn(true);
            UUID uuid = UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6");
            BankAccount bankAccount = BankAccountMock.mock();

            final LocalDateTime start = LocalDateTime.MIN;
            final LocalDateTime end = LocalDateTime.MAX;
            List<Movement> movements = Collections.singletonList(MovementMock.mock(false));

            when(movementRepository
                    .findByMovementDateBetweenAndBankAccount(start, end, bankAccount))
                    .thenReturn(movements);
            when(bankAccountService.findById(uuid)).thenReturn(bankAccount);

            List<Movement> movementReturn = movementService.findByPeriodAndBankAccount(start, end, uuid);

            assertFalse(movementReturn.isEmpty(), "Movements cannot be empty");

            verify(movementRepository).findByMovementDateBetweenAndBankAccount(start, end, bankAccount);
            verify(bankAccountService).findById(uuid);
        }
    }

    @Test
    void testFindByPeriodExpectedNoResultException() throws Exception{
        try(MockedStatic<UserUtil> mockedUserUtil = Mockito.mockStatic(UserUtil.class)) {
            mockedUserUtil.when(() -> UserUtil.hasAuthority(any(ProfileRole.class))).thenReturn(true);
            UUID uuid = UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6");
            BankAccount bankAccount = BankAccountMock.mock();

            final LocalDateTime start = LocalDateTime.MIN;
            final LocalDateTime end = LocalDateTime.MAX;

            when(movementRepository
                    .findByMovementDateBetweenAndBankAccount(start, end, bankAccount))
                    .thenReturn(Collections.emptyList());
            when(bankAccountService.findById(uuid)).thenReturn(bankAccount);

            assertThrows(NoResultException.class, () -> movementService.findByPeriodAndBankAccount(start, end, uuid));

            verify(movementRepository).findByMovementDateBetweenAndBankAccount(start, end, bankAccount);
            verify(bankAccountService).findById(uuid);
        }
    }

    @Test
    void testFindByPeriodExpectedAuthenticationFailException() throws Exception{
        try(MockedStatic<UserUtil> mockedUserUtil = Mockito.mockStatic(UserUtil.class)) {
            mockedUserUtil.when(() -> UserUtil.hasAuthority(any(ProfileRole.class))).thenReturn(false);
            UUID uuid = UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6");
            BankAccount bankAccount = BankAccountMock.mock();

            final LocalDateTime start = LocalDateTime.MIN;
            final LocalDateTime end = LocalDateTime.MAX;

            when(movementRepository
                    .findByMovementDateBetweenAndBankAccount(start, end, bankAccount))
                    .thenReturn(Collections.emptyList());
            when(bankAccountService.findById(uuid)).thenReturn(bankAccount);

            assertThrows(AuthenticationFailException.class, () -> movementService.findByPeriodAndBankAccount(start, end, uuid));

            verify(movementRepository, never()).findByMovementDateBetweenAndBankAccount(start, end, bankAccount);
            verify(bankAccountService).findById(uuid);
        }
    }


    @Test
    void testFindByPeriodWithNonexistentBankAccountExpectedNoResultException() throws Exception{
        UUID uuid = UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6");
        BankAccount bankAccount = BankAccountMock.mock();

        final LocalDateTime start = LocalDateTime.MIN;
        final LocalDateTime end = LocalDateTime.MAX;

        when(bankAccountService.findById(uuid)).thenThrow(new NoResultException("No account"));

        assertThrows(NoResultException.class, () -> movementService.findByPeriodAndBankAccount(start, end, uuid));

        verify(movementRepository, never()).findByMovementDateBetweenAndBankAccount(start, end, bankAccount);
        verify(bankAccountService).findById(uuid);
    }

}
