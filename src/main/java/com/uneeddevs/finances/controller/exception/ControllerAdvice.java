package com.uneeddevs.finances.controller.exception;

import com.uneeddevs.finances.security.exception.AuthenticationFailException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class ControllerAdvice {

    private static final String BAD_REQUEST_TEXT = "Bad request";
    private static final String NOT_ALLOWED_TEXT = "Method not allowed";
    private static final String INTERNAL_SERVER_ERROR_TEXT = "Unexpected error";
    private static final String NOT_FOUND_TEXT = "Not found";
    private static final String FORBIDDEN_TEXT = "Forbidden";

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ValidationError> badRequest(MethodArgumentNotValidException ex, HttpServletRequest request) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        ValidationError validationError = new ValidationError(LocalDateTime.now(),
                httpStatus.value(),
                BAD_REQUEST_TEXT,
                "Validation error",
                request.getRequestURI());
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors())
            validationError.addError(fieldError.getField(), fieldError.getDefaultMessage());
        return ResponseEntity.status(httpStatus).body(validationError);
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<StandardError> badRequest(IllegalArgumentException ex, HttpServletRequest request) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(httpStatus).body(StandardError.builder()
                .error(BAD_REQUEST_TEXT)
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .time(LocalDateTime.now())
                .status(httpStatus.value())
                .build());
    }

    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<StandardError> badRequest(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(httpStatus).body(StandardError.builder()
                .error(BAD_REQUEST_TEXT)
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .time(LocalDateTime.now())
                .status(httpStatus.value())
                .build());
    }

    @ExceptionHandler(value = NoResultException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ResponseEntity<StandardError> notFound(NoResultException ex, HttpServletRequest request) {
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        return ResponseEntity.status(httpStatus).body(StandardError.builder()
                .error(NOT_FOUND_TEXT)
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .time(LocalDateTime.now())
                .status(httpStatus.value())
                .build());
    }

    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<StandardError> badRequest(MissingServletRequestParameterException ex, HttpServletRequest request) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(httpStatus).body(StandardError.builder()
                .error(BAD_REQUEST_TEXT)
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .time(LocalDateTime.now())
                .status(httpStatus.value())
                .build());
    }

    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<StandardError> badRequest(HttpMessageNotReadableException ex, HttpServletRequest request) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(httpStatus).body(StandardError.builder()
                .error(BAD_REQUEST_TEXT)
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .time(LocalDateTime.now())
                .status(httpStatus.value())
                .build());
    }

    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<StandardError> notAllowed(HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {
        HttpStatus httpStatus = HttpStatus.METHOD_NOT_ALLOWED;
        return ResponseEntity.status(httpStatus).body(StandardError.builder()
                .error(NOT_ALLOWED_TEXT)
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .time(LocalDateTime.now())
                .status(httpStatus.value())
                .build());
    }

    @ExceptionHandler(value = AuthenticationFailException.class)
    public ResponseEntity<StandardError> forbidden(AuthenticationFailException ex, HttpServletRequest request) {
        HttpStatus httpStatus = HttpStatus.FORBIDDEN;
        return ResponseEntity.status(httpStatus).body(StandardError.builder()
                .error(FORBIDDEN_TEXT)
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .time(LocalDateTime.now())
                .status(httpStatus.value())
                .build());
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<StandardError> internalServerError(Exception ex, HttpServletRequest request) {
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        log.error("Unexpected error occurred", ex);
        return ResponseEntity.status(httpStatus).body(StandardError.builder()
                .error(INTERNAL_SERVER_ERROR_TEXT)
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .time(LocalDateTime.now())
                .status(httpStatus.value())
                .build());
    }


}
