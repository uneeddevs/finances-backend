package com.uneeddevs.finances.dto;

import com.uneeddevs.finances.util.CheckUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.lang.NonNull;

import java.math.BigDecimal;
import java.util.UUID;

import static com.uneeddevs.finances.util.CheckUtils.*;

@Schema(name = "Bank account response")
public record BankAccountResponseDTO(
        @Schema(example = "3fa85f64-5717-4562-b3fc-2c963f66afa6", name = "id") UUID id,
        @Schema(example = "Pretty account name", name = "name") String name,
        @Schema(example = "100.0", name = "balance") BigDecimal balance) {

    public BankAccountResponseDTO(@NonNull UUID id,
                                  @NonNull String name,
                                  @NonNull BigDecimal balance) {
        this.balance = requirePositive(CheckUtils.requireNonNull(balance, "balance is mandatory"),
                "Initial balance cannot be negative");
        this.name = requireNotBlank(name, "Account name cannot be empty or null");
        this.id = requireNonNull(id, "id is mandatory");
    }
}
