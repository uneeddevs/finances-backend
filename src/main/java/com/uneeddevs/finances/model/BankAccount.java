package com.uneeddevs.finances.model;

import com.uneeddevs.finances.dto.BankAccountResponseDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.*;

import static com.uneeddevs.finances.util.CheckUtils.*;

@Entity
@Getter
@ToString
@Table(name = "TB_BANK_ACCOUNT")
@NoArgsConstructor(onConstructor = @__(@Deprecated))
public class BankAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(updatable = false, unique = true, nullable = false)
    private UUID id;
    @Column(nullable = false)
    private BigDecimal balance;
    @Setter
    @Column(length = 100, nullable = false)
    private String name;
    @ManyToOne
    @ToString.Exclude
    @JoinColumn(nullable = false)
    private User user;
    @OneToMany
    @ToString.Exclude
    @JoinColumn(name = "BANK_ACCOUNT_ID")
    private final Set<Movement> movements = new HashSet<>();

    public BankAccount(@NonNull UUID id,
                       @NonNull String name) {
        this.id = id;
        this.name = requireNotBlank(name, "Account name cannot be empty or null");
    }

    public BankAccount(@NonNull BigDecimal balance,
                       @NonNull String name,
                       @NonNull User user) {
        this.balance = requirePositive(requireNonNull(balance, "initial balance is mandatory"),
                "Initial balance cannot be negative");
        this.name = requireNotBlank(name, "Account name cannot be empty or null");
        this.user = requireNonNull(user, "User is mandatory");
    }

    public UUID getUserId() {
        return user.getId();
    }

    public void subtractBalance(@NonNull BigDecimal value) {
        requireNonNull(requirePositive(value, "Value to add cannot be negative"),
                "Value cannot be null");
        if(value.doubleValue() > balance.doubleValue())
            throw new IllegalArgumentException("Value to subtract cannot be greater than balance");
        balance = balance.subtract(value);
    }

    public void addBalance(@NonNull BigDecimal value) {
        balance = balance.add(requireNonNull(requirePositive(value, "Value to add cannot be negative"),
                "Value cannot be null"));
    }

    public Set<Movement> getMovements() {
        return Collections.unmodifiableSet(movements);
    }

    public void addMovement(Movement movement) {
        movements.add(requireNonNull(movement, "Movement cannot be null"));
    }

    public BankAccountResponseDTO toBankAccountResponseDTO(){
        return new BankAccountResponseDTO(id, name, balance);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BankAccount that = (BankAccount) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
