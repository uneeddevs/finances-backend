package com.uneeddevs.finances.controller.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NonNull;

@Getter
@Schema(name = "Field Message used in Validation Error", description = "Fields with error messages")
public class FieldMessage {


    @Schema(name = "field", example = "fieldName")
    private final String fieldName;
    @Schema(name = "messsage", example = "validation error message")
    private final String message;

    public FieldMessage(@NonNull String fieldName, @NonNull String message) {
        this.fieldName = fieldName;
        this.message = message;
    }


}
