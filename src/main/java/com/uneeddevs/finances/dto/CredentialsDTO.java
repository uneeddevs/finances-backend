package com.uneeddevs.finances.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(name = "Credentials")
public class CredentialsDTO {

    @Schema(name = "email", example = "user@mail.com")
    private String email;
    @Schema(name = "password", example = "verystrongpassword")
    private String password;



}
