package com.uneeddevs.finances.controller.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;

import static java.util.Objects.requireNonNull;

@Getter
@Builder
@Schema(name = "Standard Error", description = "Error responses")
public class StandardError {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "GMT")
    private final LocalDateTime time;
    @Schema(name = "status", example = "error_code")
    private final Integer status;
    @Schema(name = "error", example = "error")
    private final String error;
    @Schema(name = "message", example = "error message")
    private final String message;
    @Schema(name = "message", example = "/documentation")
    private final String path;

    public StandardError(@NonNull LocalDateTime time,
                         @NonNull Integer status,
                         @NonNull String error,
                         @NonNull String message,
                         @NonNull String path) {
        this.time = requireNonNull(time, "time is mandatory");
        this.status = requireNonNull(status, "status is mandatory");
        this.error = requireNonNull(error, "error is mandatory");
        this.message = requireNonNull(message, "message is mandatory");
        this.path = requireNonNull(path, "path is mandatory");
    }

}
