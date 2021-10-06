package com.uneeddevs.finances.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static com.uneeddevs.finances.util.CheckUtils.requireNonNull;
import static com.uneeddevs.finances.util.CheckUtils.requireNotBlank;

@Getter
@Schema(name = "User response", description = "User request response payload")
public class UserResponseDTO {

    @Schema(example = "3fa85f64-5717-4562-b3fc-2c963f66afa6", name = "id")
    private final UUID id;
    @Schema(example = "Pretty Name", name = "name")
    private final String name;
    @Schema(example = "user@mail.com", name = "email")
    private final String email;
    @Schema(example = "2021-09-20T15:18:54.708Z", name = "registerDate")
    private final LocalDateTime registerDate;
    private final Set<String> profiles = new HashSet<>();

    public UserResponseDTO(@NonNull UUID id,
                           @NonNull String name,
                           @NonNull String email,
                           @NonNull LocalDateTime registerDate) {
        this.id = requireNonNull(id, "id is mandatory");
        this.registerDate = requireNonNull(registerDate, "register date is mandatory");
        this.name = requireNotBlank(name, "Name is mandatory");
        this.email = requireNotBlank(email, "Email is mandatory");
    }

    public void addProfile(String profile) {
        profiles.add(requireNotBlank(profile, "Profile cannot be null"));
    }
}
