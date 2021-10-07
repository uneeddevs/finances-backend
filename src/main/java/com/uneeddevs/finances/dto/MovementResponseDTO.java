package com.uneeddevs.finances.dto;

import com.uneeddevs.finances.enums.MovementType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record MovementResponseDTO(UUID id,
         MovementType movementType,
         BigDecimal value,
         LocalDateTime movementDate) {
}
