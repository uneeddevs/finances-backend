package com.uneeddevs.finances.dto;

import com.uneeddevs.finances.enums.MovementType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(name = "Movement Response")
public record MovementResponseDTO(
        @Schema(name = "id", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
        UUID id,
        @Schema(name = "movementType", example = "INPUT")
        MovementType movementType,
        @Schema(name = "value", example = "48.75")
        BigDecimal value,
        @Schema(name = "movementDate", example = "2021-10-06T20:43:03")
        LocalDateTime movementDate) {
}
