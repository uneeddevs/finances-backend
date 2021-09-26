package com.uneeddevs.finances.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Entity
@Getter
@ToString
@Table(name = "TB_PROFILE")
@NoArgsConstructor(onConstructor = @__(@Deprecated))
public class Profile {

    @Id
    private Long id;
    @Column(nullable = false, length = 35, unique = true)
    private String roleName;

    public Profile(@NonNull String roleName) {
        if(isBlank(roleName))
            throw new IllegalArgumentException("Role Name is mandatory");
        this.roleName = roleName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Profile profile = (Profile) o;
        return Objects.equals(roleName, profile.roleName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roleName);
    }
}
