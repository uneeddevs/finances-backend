package com.uneeddevs.finances.repository;

import com.uneeddevs.finances.model.BankAccount;
import com.uneeddevs.finances.model.Movement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface MovementRepository extends JpaRepository<Movement, UUID> {

    List<Movement> findByMovementDateBetweenAndBankAccount(LocalDateTime start, LocalDateTime end, BankAccount bankAccount);
}
