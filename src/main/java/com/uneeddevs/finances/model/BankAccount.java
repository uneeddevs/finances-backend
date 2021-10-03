package com.uneeddevs.finances.model;

import com.uneeddevs.finances.dto.BankAccountResponseDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

import static org.apache.commons.lang3.StringUtils.isBlank;

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

    public BankAccount(@NonNull UUID id,
                       @NonNull String name) {
        this.id = id;
        if(isBlank(name))
            throw new IllegalArgumentException("Account name cannot be empty or null");
        this.name = name;
    }

    public BankAccount(@NonNull BigDecimal balance,
                       @NonNull String name,
                       @NonNull User user) {
        if(balance.doubleValue() < 0)
            throw new IllegalArgumentException("Initial balance cannot be negative");
        this.balance = balance;
        if(isBlank(name))
            throw new IllegalArgumentException("Account name cannot be empty or null");
        this.name = name;
        this.user = user;
    }

    public UUID getUserId() {
        return user.getId();
    }

    public void subtractBalance(@NonNull BigDecimal value) {
        if(value.doubleValue() < 0)
            throw new IllegalArgumentException("Value to subtract cannot be negative");
        if(value.doubleValue() > balance.doubleValue())
            throw new IllegalArgumentException("Value to subtract cannot be greater than balance");
        balance = balance.subtract(value);
    }

    public void addBalance(@NonNull BigDecimal value) {
        if(value.doubleValue() < 0)
            throw new IllegalArgumentException("Value to add cannot be negative");
        balance = balance.add(value);
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
