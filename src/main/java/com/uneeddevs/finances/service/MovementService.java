package com.uneeddevs.finances.service;

import com.uneeddevs.finances.model.Movement;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface MovementService {

    Movement save(Movement movement);
    Movement findById(UUID id);
    List<Movement> findByPeriodAndBankAccount(LocalDateTime start, LocalDateTime end, UUID bankAccountId);
    void deleteMovementById(UUID id);

}
