package com.uneeddevs.finances.dto;

import com.uneeddevs.finances.model.BankAccount;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import java.util.UUID;

@Getter
@Schema(name = "Bank account update")
public class BankAccountUpdateDTO {

    @Schema(name = "accountName", example = "Update Pretty Name Account")
    @NotBlank(message = "Account name cannot be null or empty")
    private String accountName;

    public BankAccount toModel(UUID uuid) {
        return new BankAccount(uuid, accountName);
    }

}
