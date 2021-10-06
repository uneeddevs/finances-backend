package com.uneeddevs.finances.dto;

import com.uneeddevs.finances.enums.MovementType;
import com.uneeddevs.finances.model.BankAccount;
import com.uneeddevs.finances.model.Movement;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.UUID;

public record MovementInsertDTO(@NotNull(message = "value is mandatory")
                                @Positive(message = "Invalid value")
                                BigDecimal value,
                                @NotNull(message = "bank account id is mandatory")
                                UUID bankAccountId) {

    public  Movement toModel(MovementType movementType) {
        return new Movement(movementType, value, new BankAccount(bankAccountId, "insert"));
    }
}
