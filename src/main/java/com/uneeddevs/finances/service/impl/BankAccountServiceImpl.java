package com.uneeddevs.finances.service.impl;

import com.uneeddevs.finances.constants.Messages;
import com.uneeddevs.finances.enums.ProfileRole;
import com.uneeddevs.finances.model.BankAccount;
import com.uneeddevs.finances.model.User;
import com.uneeddevs.finances.repository.BankAccountRepository;
import com.uneeddevs.finances.security.exception.AuthenticationFailException;
import com.uneeddevs.finances.service.BankAccountService;
import com.uneeddevs.finances.service.UserService;
import com.uneeddevs.finances.util.UserUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class BankAccountServiceImpl implements BankAccountService {

    private final BankAccountRepository bankAccountRepository;
    private final UserService userService;

    @Override
    public List<BankAccount> findByUser(User user) {
        final UUID userId = user.getId();
        if(!UserUtil.hasAuthority(ProfileRole.ADMIN) &&
                !userId.equals(UserUtil.authenticatedUUID()))
            throw new AuthenticationFailException(Messages.FORBIDDEN_TEXT);

        User userResponse = userService.findById(userId);
        List<BankAccount> bankAccounts = bankAccountRepository.findByUser(userResponse);
        if(!bankAccounts.isEmpty())
            return  bankAccounts;
        throw new NoResultException(String.format("No bank accounts for user %s", userId));
    }

    @Override
    public BankAccount save(BankAccount bankAccount) {
        log.info("Performing persistent bank account {}", bankAccount);
        return bankAccountRepository.save(bankAccount);
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
        final BankAccount bankAccount = bankAccountRepository.findById(id)
                .orElseThrow(() -> {
                    String message = String.format("No bank account with id %s", id);
                    log.info(message);
                    if(UserUtil.hasAuthority(ProfileRole.ADMIN))
                        throw new NoResultException(message);
                    return new AuthenticationFailException(Messages.FORBIDDEN_TEXT);
                });
        final UUID userId = bankAccount.getUserId();
        if(!UserUtil.hasAuthority(ProfileRole.ADMIN) &&
                !userId.equals(UserUtil.authenticatedUUID()))
            throw new AuthenticationFailException(Messages.FORBIDDEN_TEXT);
        return bankAccount;
    }

    @Override
    public boolean deleteById(UUID id) {
        log.info("Performing deleting bank account by id {}", id);
        BankAccount bankAccount = findById(id);
        bankAccountRepository.delete(bankAccount);
        return true;
    }
}
