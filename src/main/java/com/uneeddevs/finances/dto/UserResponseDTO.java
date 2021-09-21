package com.uneeddevs.finances.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.apache.commons.lang3.StringUtils.isBlank;

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

    public UserResponseDTO(@NonNull UUID id,
                           @NonNull String name,
                           @NonNull String email,
                           @NonNull LocalDateTime registerDate) {
        this.id = id;
        this.registerDate = registerDate;
        if(isBlank(name))
            throw new IllegalArgumentException("Name is mandatory");
        this.name = name;
        if(isBlank(email))
            throw new IllegalArgumentException("Email is mandatory");
        this.email = email;
    }
}
