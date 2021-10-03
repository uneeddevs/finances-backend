package com.uneeddevs.finances.model;

import com.uneeddevs.finances.dto.UserResponseDTO;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

import static java.util.Objects.isNull;
import static org.apache.commons.lang3.StringUtils.isBlank;

@Entity
@Getter
@ToString
@Table(name = "TB_USER")
@NoArgsConstructor(onConstructor = @__(@Deprecated))
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(updatable = false, unique = true, nullable = false)
    private UUID id;
    @Setter
    @Column(nullable = false, length = 70)
    private String name;
    @Column(nullable = false, length = 100, unique = true)
    private String email;
    @Lob
    @Setter
    @Column(nullable = false)
    @ToString.Exclude
    private String password;
    @CreationTimestamp
    private LocalDateTime registerDate;
    @UpdateTimestamp
    private LocalDateTime updateDate;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(joinColumns = @JoinColumn(name = "USER_ID", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "PROFILE_ID", nullable = false)
    )
    @ToString.Exclude
    private final Set<Profile> profiles = new HashSet<>();
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "USER_ID")
    @ToString.Exclude
    private final Set<BankAccount> bankAccounts = new HashSet<>();

    public User(UUID uuid, @NonNull String name, @NonNull String password) {
        this.id = uuid;
        if(isBlank(name))
            throw new IllegalArgumentException("Name is mandatory");
        this.name = name;
        if(isBlank(password))
            throw new IllegalArgumentException("Password is mandatory");
        this.password = password;
    }

    public User(@NonNull String name, @NonNull String email, @NonNull String password) {
        if(isBlank(name))
            throw new IllegalArgumentException("Name is mandatory");
        this.name = name;
        if(isBlank(email))
            throw new IllegalArgumentException("Email is mandatory");
        this.email = email;
        if(isBlank(password))
            throw new IllegalArgumentException("Password is mandatory");
        this.password = password;
    }

    public Set<Profile> getProfiles() {
        return Collections.unmodifiableSet(profiles);
    }

    public Set<BankAccount> getBankAccounts() {
        return Collections.unmodifiableSet(bankAccounts);
    }

    public void addBankAccount(BankAccount bankAccount) {
        if(isNull(bankAccount))
            throw new IllegalArgumentException("Bank account cannot be null");
        bankAccounts.add(bankAccount);
    }

    public void addProfile(Profile profile) {
        if(isNull(profile))
            throw new IllegalArgumentException("Profile cannot be null");
        profiles.add(profile);
    }

    public UserResponseDTO toUserResponseDTO() {
        UserResponseDTO userResponseDTO = new UserResponseDTO(id, name, email, registerDate);
        for(var profile: profiles)
            userResponseDTO.addProfile(profile.getRoleName());
        return userResponseDTO;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
