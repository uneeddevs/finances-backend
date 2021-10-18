package com.uneeddevs.finances.dto;

import com.uneeddevs.finances.model.User;
import com.uneeddevs.finances.service.validation.UserInsert;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@UserInsert
@NoArgsConstructor(onConstructor = @__(@Deprecated))
@Schema(name = "User insert", description = "User insertion request payload")
public class UserInsertDTO {

    @Email
    @NotBlank(message = "Email is mandatory")
    @Schema(example = "user@mail.com", name = "email", required = true)
    private String email;
    @NotBlank(message = "Name is mandatory")
    @Schema(example = "Pretty Name", name = "name", required = true)
    private String name;
    @NotBlank(message = "Password is mandatory")
    @Schema(example = "sercret123", name = "password", required = true)
    private String password;

    public UserInsertDTO(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public User toModel() {
        return new User(name, email, new BCryptPasswordEncoder().encode(password));
    }
}
