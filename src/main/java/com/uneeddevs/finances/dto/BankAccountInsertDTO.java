package com.uneeddevs.finances.dto;

import com.uneeddevs.finances.model.BankAccount;
import com.uneeddevs.finances.model.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Schema(name = "Bank account insert")
public class BankAccountInsertDTO {

    @Schema(example = "Account name", name = "name", required = true)
    @NotBlank(message = "Name is mandatory")
    private String name;
    @NotNull(message = "User id cannot be null")
    @Schema(example = "3fa85f64-5717-4562-b3fc-2c963f66afa6", name = "userId", required = true)
    private UUID userId;
    @PositiveOrZero(message = "Initial balance cannot cannot be negative")
    @NotNull(message = "Initial balance cannot be null")
    @Schema(example = "100.0", name = "initialBalance", required = true)
    private BigDecimal initialBalance;

    public BankAccount toModel() {
        User user = new User(userId, "name", "pass");
        return new BankAccount(initialBalance, name, user);
    }

}
