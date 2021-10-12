package com.uneeddevs.finances.model;

import com.uneeddevs.finances.dto.UserResponseDTO;
import com.uneeddevs.finances.util.CheckUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.lang.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.uneeddevs.finances.util.CheckUtils.requireNonNull;
import static com.uneeddevs.finances.util.CheckUtils.requireNotBlank;
import static java.util.Objects.isNull;

@Entity
@Getter
@ToString
@Table(name = "TB_USER")
@NoArgsConstructor(onConstructor = @__(@Deprecated))
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(updatable = false, unique = true, nullable = false)
    private UUID id;
    @Setter
    @Column(nullable = false, length = 70)
    private String name;
    @Column(nullable = false, length = 100, unique = true)
    private String email;
    @Setter
    @Column(nullable = false, columnDefinition = "TEXT")
    @ToString.Exclude
    private String password;
    @CreationTimestamp
    private LocalDateTime registerDate;
    @UpdateTimestamp
    private LocalDateTime updateDate;
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(joinColumns = @JoinColumn(name = "USER_ID", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "PROFILE_ID", nullable = false)
    )
    @ToString.Exclude
    private final Set<Profile> profiles = new HashSet<>();
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "USER_ID")
    @ToString.Exclude
    private final Set<BankAccount> bankAccounts = new HashSet<>();

    public User(UUID uuid) {
        this.id = requireNonNull(uuid, "id is mandatory");
    }

    public User(UUID uuid, @NonNull String name, @NonNull String password) {
        this.id = uuid;
        this.name = requireNotBlank(name, "Name is mandatory");
        this.password = requireNotBlank(password, "Password is mandatory");
    }

    public User(@NonNull String name, @NonNull String email, @NonNull String password) {
        this.name = requireNotBlank(name, "Name is mandatory");
        this.email = requireNotBlank(email, "Email is mandatory");
        this.password = requireNotBlank(password, "Password is mandatory");
    }

    public Set<Profile> getProfiles() {
        return Collections.unmodifiableSet(profiles);
    }

    public Set<BankAccount> getBankAccounts() {
        return Collections.unmodifiableSet(bankAccounts);
    }

    public void addBankAccount(BankAccount bankAccount) {
        bankAccounts.add(CheckUtils.requireNonNull(bankAccount, "Bank account cannot be null"));
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
        return Objects.hash(id);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return profiles.stream()
                .map(profile -> new SimpleGrantedAuthority(profile.getRoleName()))
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
