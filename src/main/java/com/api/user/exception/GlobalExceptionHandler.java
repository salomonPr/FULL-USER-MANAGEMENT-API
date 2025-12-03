package com.api.user.exception;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiError> handleUserNotFoundException(UserNotFoundException ex, WebRequest request) {
        log.error("User not found: {}", ex.getMessage());
        ApiError body = ApiError.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error("User Not Found")
                .message(ex.getMessage())
                .path(null)
                .build();
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicationUserException.class)
    public ResponseEntity<ApiError> handleDuplicateUserException(DuplicationUserException ex, WebRequest request) {
        log.error("Duplicate user data: {}", ex.getMessage());
        ApiError body = ApiError.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.CONFLICT.value())
                .error("Duplicate Data")
                .message(ex.getMessage())
                .path(null)
                .build();
        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationExceptions(MethodArgumentNotValidException ex) {
        log.error("Validation error: {}", ex.getMessage());
        List<String> validationErrors = new ArrayList<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            validationErrors.add(error.getField() + ": " + error.getDefaultMessage());
        });

        Map<String, Object> details = new HashMap<>();
        details.put("errors", validationErrors);

        ApiError body = ApiError.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Invalid Input")
                .message("Validation failed")
                .path(null)
                .details(details)
                .build();

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> handleConstraintViolationException(ConstraintViolationException ex) {
        log.error("Constraint violation: {}", ex.getMessage());
        List<String> validationErrors = new ArrayList<>();

        ex.getConstraintViolations().forEach(violation -> {
            validationErrors.add(violation.getPropertyPath() + ": " + violation.getMessage());
        });

        Map<String, Object> details = new HashMap<>();
        details.put("errors", validationErrors);

        ApiError body = ApiError.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Invalid Input")
                .message("Constraint validation failed")
                .path(null)
                .details(details)
                .build();

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        log.error("Data integrity violation: {}", ex.getMessage());
        String message = "Data integrity violation. This might be due to duplicate unique values.";

        if (ex.getMessage().contains("username")) {
            message = "Username already exists";
        } else if (ex.getMessage().contains("email")) {
            message = "Email already exists";
        } else if (ex.getMessage().contains("phone")) {
            message = "Phone number already exists";
        }

        ApiError body = ApiError.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.CONFLICT.value())
                .error("Data Integrity Error")
                .message(message)
                .path(null)
                .build();
        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGenericException(Exception ex, WebRequest request) {
        log.error("Unexpected error occurred: {}", ex.getMessage(), ex);
        ApiError body = ApiError.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error("Internal Server Error")
                .message("An unexpected error occurred")
                .path(null)
                .build();
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}