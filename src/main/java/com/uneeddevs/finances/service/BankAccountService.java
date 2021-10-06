package com.uneeddevs.finances.service;

import com.uneeddevs.finances.model.BankAccount;
import com.uneeddevs.finances.model.User;

import java.util.List;
import java.util.UUID;

public interface BankAccountService {

    List<BankAccount> findByUser(User user);
    BankAccount save(BankAccount bankAccount);
    BankAccount update(BankAccount bankAccount);
    BankAccount findById(UUID id);
    boolean deleteById(UUID id);

}
