package com.uneeddevs.finances.model;

import com.uneeddevs.finances.dto.UserResponseDTO;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Entity
@Getter
@ToString
@Table(name = "TB_USER")
@NoArgsConstructor(onConstructor = @__(@Deprecated))
public class User {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(updatable = false, unique = true, nullable = false)
    private UUID id;
    @Column(nullable = false, length = 70)
    private String name;
    @Column(nullable = false, length = 100, unique = true)
    private String email;
    @Lob
    @Column(nullable = false)
    @ToString.Exclude
    private String password;
    @CreationTimestamp
    private LocalDateTime registerDate;

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

    public UserResponseDTO toUserResponseDTO() {
        return new UserResponseDTO(id, name, email, registerDate);
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
