package com.uneeddevs.finances.service.impl;

import com.uneeddevs.finances.constants.Messages;
import com.uneeddevs.finances.enums.MovementType;
import com.uneeddevs.finances.enums.ProfileRole;
import com.uneeddevs.finances.model.BankAccount;
import com.uneeddevs.finances.model.Movement;
import com.uneeddevs.finances.repository.MovementRepository;
import com.uneeddevs.finances.security.exception.AuthenticationFailException;
import com.uneeddevs.finances.service.BankAccountService;
import com.uneeddevs.finances.service.MovementService;
import com.uneeddevs.finances.util.UserUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MovementServiceImpl implements MovementService {

    private final MovementRepository movementRepository;
    private final BankAccountService bankAccountService;

    @Override
    public Movement save(Movement movement) {
        final UUID bankAccountId = movement.getBankAccountId();
        log.info("Performing account search for id: {}", bankAccountId);
        BankAccount bankAccount = bankAccountService.findById(bankAccountId);
        final BigDecimal movementValue = movement.getValue();
        log.info("Validating movement typ and perform balance update");
        if (movement.getMovementType() == MovementType.INPUT)
            bankAccount.addBalance(movementValue);
        else
            bankAccount.subtractBalance(movementValue);

        log.info("Performing movement save");
        final Movement persistedMovement = movementRepository.save(movement);
        saveBankAccount(bankAccount);
        return persistedMovement;
    }

    @Override
    public Movement findById(UUID id) {
        log.info("Searching movement by id {}", id);
        final Movement movement = movementRepository.findById(id).orElseThrow(() -> {
            final String message = String.format("No movement with id %s", id);
            log.warn(message);
            if (UserUtil.hasAuthority(ProfileRole.ADMIN))
                return new NoResultException(message);
            throw new AuthenticationFailException(Messages.FORBIDDEN_TEXT);
        });
        final BankAccount bankAccount = movement.getBankAccount();
        final UUID userId = bankAccount.getUserId();
        if(!UserUtil.hasAuthority(ProfileRole.ADMIN) &&
                !userId.equals(UserUtil.authenticatedUUID()))
            throw new AuthenticationFailException(Messages.FORBIDDEN_TEXT);
        return movement;
    }

    @Override
    public void deleteMovementById(UUID id) {
        log.info("Performing delete movement by id {}", id);
        final Movement movement = findById(id);
        final BankAccount bankAccount = movement.getBankAccount();
        final BigDecimal movementValue = movement.getValue();
        if(movement.getMovementType() == MovementType.INPUT)
            bankAccount.subtractBalance(movementValue);
        else
            bankAccount.addBalance(movementValue);
        saveBankAccount(bankAccount);
        movementRepository.delete(movement);
    }

    private void saveBankAccount(BankAccount bankAccount) {
        bankAccountService.save(bankAccount);
    }

    @Override
    public List<Movement> findByPeriodAndBankAccount(LocalDateTime start, LocalDateTime end, UUID bankAccountId) {
        log.info("Performing search movement by account {} between {} and {}", bankAccountId, start, end);
        final BankAccount bankAccount = bankAccountService.findById(bankAccountId);
        final UUID userId = bankAccount.getUserId();
        if(!UserUtil.hasAuthority(ProfileRole.ADMIN) &&
                !userId.equals(UserUtil.authenticatedUUID()))
            throw new AuthenticationFailException(Messages.FORBIDDEN_TEXT);
        List<Movement> movements = movementRepository.findByMovementDateBetweenAndBankAccount(start, end, bankAccount);
        if(!movements.isEmpty())
            return movements;
        final String message = String.format("No movements for account %s between %s and %s", bankAccountId, start, end);
        log.warn(message);
        throw new NoResultException(message);
    }
}
