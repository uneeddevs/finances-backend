package com.uneeddevs.finances.service;

import com.uneeddevs.finances.model.BankAccount;

import java.util.UUID;

public interface BankAccountService {

    BankAccount insert(BankAccount bankAccount);
    BankAccount update(BankAccount bankAccount);
    BankAccount findById(UUID id);
    boolean deleteById(UUID id);

}
