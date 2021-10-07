package com.uneeddevs.finances.dto;

import com.uneeddevs.finances.enums.MovementType;
import com.uneeddevs.finances.model.BankAccount;
import com.uneeddevs.finances.model.Movement;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.UUID;

@Schema(name = "Movement insert")
public record MovementInsertDTO(
        @Schema(name = "value", example = "45.78")
        @NotNull(message = "value is mandatory")
        @Positive(message = "Invalid value")
        BigDecimal value,
        @Schema(name = "bankAccountId", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
        @NotNull(message = "bank account id is mandatory")
        UUID bankAccountId) {

    public  Movement toModel(MovementType movementType) {
        return new Movement(movementType, value, new BankAccount(bankAccountId, "insert"));
    }
}
