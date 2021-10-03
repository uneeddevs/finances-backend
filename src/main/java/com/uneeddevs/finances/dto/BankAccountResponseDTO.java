package com.uneeddevs.finances.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.springframework.lang.NonNull;

import java.math.BigDecimal;
import java.util.UUID;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Getter
@Schema(name = "Bank account response")
public class BankAccountResponseDTO {

    @Schema(example = "3fa85f64-5717-4562-b3fc-2c963f66afa6", name = "id")
    private final UUID id;
    @Schema(example = "Pretty account name", name = "name")
    private final String name;
    @Schema(example = "100.0", name = "balance")
    private final BigDecimal balance;

    public BankAccountResponseDTO(@NonNull UUID id,
                                  @NonNull String name,
                                  @NonNull BigDecimal balance) {
        if(balance.doubleValue() < 0)
            throw new IllegalArgumentException("Initial balance cannot be negative");
        this.balance = balance;
        if(isBlank(name))
            throw new IllegalArgumentException("Account name cannot be empty or null");
        this.name = name;
        this.id = id;
    }
}
