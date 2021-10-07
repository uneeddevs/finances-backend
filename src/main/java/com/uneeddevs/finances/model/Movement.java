package com.uneeddevs.finances.model;

import com.uneeddevs.finances.dto.MovementResponseDTO;
import com.uneeddevs.finances.enums.MovementType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static com.uneeddevs.finances.util.CheckUtils.requireNonNull;
import static com.uneeddevs.finances.util.CheckUtils.requirePositive;

@Getter
@Entity
@Table(name = "TB_MOVEMENT")
@NoArgsConstructor(onConstructor = @__(@Deprecated))
public class Movement {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(updatable = false, unique = true, nullable = false)
    private UUID id;
    @Column(nullable = false)
    private Integer movementType;
    @Column(nullable = false)
    private BigDecimal value;
    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime movementDate;
    @ManyToOne
    @JoinColumn(nullable = false)
    private BankAccount bankAccount;

    public Movement(@NonNull MovementType movementType,
                    @NonNull BigDecimal value,
                    @NonNull BankAccount bankAccount) {
        this.movementType = requirePositive(requireNonNull(movementType, "movement type is mandatory").getValue(),
                "invalid movement type value");
        this.value = requirePositive(requireNonNull(value, "value is mandatory"),
                "value has to be a positive value");
        this.bankAccount = requireNonNull(bankAccount, "bank account is mandatory");
    }

    public UUID getBankAccountId() {
        return  bankAccount.getId();
    }

    public MovementType getMovementType() {
        return MovementType.valueOf(movementType);
    }

    public MovementResponseDTO toMovementResponseDTO() {
        return new MovementResponseDTO(id, MovementType.valueOf(movementType), value, movementDate);
    }
}
