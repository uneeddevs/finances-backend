package com.uneeddevs.finances.repository;

import com.uneeddevs.finances.model.BankAccount;
import com.uneeddevs.finances.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BankAccountRepository extends JpaRepository<BankAccount, UUID> {

    List<BankAccount> findByUser(User user);

}
