package com.uneeddevs.finances.dto;

import com.uneeddevs.finances.model.BankAccount;
import com.uneeddevs.finances.util.UserUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

import static com.uneeddevs.finances.util.CheckUtils.requireNonNull;

@Getter
@Schema(name = "Bank account insert")
public class BankAccountInsertDTO {

    @Schema(example = "Account name", name = "name", required = true)
    @NotBlank(message = "Name is mandatory")
    private String name;
    @PositiveOrZero(message = "Initial balance cannot cannot be negative")
    @NotNull(message = "Initial balance cannot be null")
    @Schema(example = "100.0", name = "initialBalance", required = true)
    private BigDecimal initialBalance;

    public BankAccount toModel() {
        return new BankAccount(initialBalance, name, requireNonNull(UserUtil.authenticated(), "User is mandatory"));
    }

}
