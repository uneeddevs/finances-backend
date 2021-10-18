package com.uneeddevs.finances.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Builder
@Schema(name = "Token")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(onConstructor = @__(@Deprecated))
public class TokenDTO {

    @Schema(name = "token", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c")
    private String token;
    @Schema(name = "expiration", example = "10000000")
    private Long expiration;

}
