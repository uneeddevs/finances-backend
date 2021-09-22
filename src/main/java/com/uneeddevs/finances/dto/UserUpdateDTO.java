package com.uneeddevs.finances.dto;

import com.uneeddevs.finances.model.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.UUID;

@Getter
@NoArgsConstructor(onConstructor = @__(@Deprecated))
@Schema(name = "User update", description = "User update request payload")
public class UserUpdateDTO {

    @NotBlank(message = "Name is mandatory")
    @Schema(example = "Pretty Name", name = "name", required = true)
    private String name;
    @NotBlank(message = "Password is mandatory")
    @Schema(example = "sercret123", name = "password", required = true)
    private String password;

    public UserUpdateDTO(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public User toModel(UUID uuid) {
        return new User(uuid, name, password);
    }
}
