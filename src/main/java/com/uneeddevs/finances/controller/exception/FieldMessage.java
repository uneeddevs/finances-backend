package com.uneeddevs.finances.controller.exception;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "Field Message used in Validation Error", description = "Fields with error messages")
public record FieldMessage(@Schema(name = "field", example = "fieldName") String fieldName,
                           @Schema(name = "message", example = "validation error message") String message) {

}
