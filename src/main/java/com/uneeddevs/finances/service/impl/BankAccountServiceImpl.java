package com.uneeddevs.finances.service.impl;

import com.uneeddevs.finances.model.BankAccount;
import com.uneeddevs.finances.repository.BankAccountRepository;
import com.uneeddevs.finances.service.BankAccountService;
import com.uneeddevs.finances.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class BankAccountServiceImpl implements BankAccountService {

    private final BankAccountRepository bankAccountRepository;
    private final UserService userService;


    private BankAccount save(BankAccount bankAccount) {
        log.info("Performing persisting for bank account {}", bankAccount);
        return bankAccountRepository.save(bankAccount);
    }

    @Override
    public BankAccount insert(BankAccount bankAccount) {
        userService.findById(bankAccount.getUserId());
        return save(bankAccount);
    }

    @Override
    public BankAccount update(BankAccount bankAccount) {
        log.info("Perform bank account update: {}", bankAccount);
        BankAccount oldAccount = findById(bankAccount.getId());
        updateInfo(oldAccount, bankAccount);
        return save(oldAccount);
    }

    private void updateInfo(BankAccount oldAccount, BankAccount newAccount) {
        oldAccount.setName(newAccount.getName());
    }

    @Override
    public BankAccount findById(UUID id) {
        log.info("Searching bank account by id: {}", id);
        return bankAccountRepository.findById(id)
                .orElseThrow(() -> {
                    String message = String.format("No bank account with id %s", id);
                    log.info(message);
                    return new NoResultException(message);
                });
    }

    @Override
    public boolean deleteById(UUID id) {
        log.info("Performing deleting bank account by id {}", id);
        BankAccount bankAccount = findById(id);
        bankAccountRepository.delete(bankAccount);
        return true;
    }
}
