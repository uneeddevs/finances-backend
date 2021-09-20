package com.uneeddevs.finances.controller.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Schema(name = "Validation Error", description = "Validation error with fields")
public class ValidationError extends StandardError{

    private final List<FieldMessage> errors = new ArrayList<>();

    public ValidationError(LocalDateTime time, Integer status, String error, String message, String path) {
        super(time, status, error, message, path);
    }

    public void addError(String fieldName, String message){
        errors.add(new FieldMessage(fieldName, message));
    }

}
