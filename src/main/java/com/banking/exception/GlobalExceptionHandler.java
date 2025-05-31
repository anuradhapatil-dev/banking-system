package com.banking.exception;

import com.banking.dto.AccountResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AccountAlreadyExistsException.class)
    public ResponseEntity<AccountResponseDTO> handleAccountAlreadyExists(AccountAlreadyExistsException ex) {
        AccountResponseDTO response = AccountResponseDTO.builder()
                .accountNumber(null)
                .message(ex.getMessage())
                .statusCode(HttpStatus.CONFLICT.value())
                .timestamp(Instant.now())
                .additionalData(null)
                .build();

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<AccountResponseDTO> handleValidationException(MethodArgumentNotValidException ex) {
        StringBuilder errors = new StringBuilder();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.append(error.getField()).append(": ").append(error.getDefaultMessage()).append("; ")
        );

        AccountResponseDTO response = AccountResponseDTO.builder()
                .accountNumber(null)
                .message(errors.toString())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .timestamp(Instant.now())
                .additionalData(null)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<AccountResponseDTO> handleIllegalArgumentException(IllegalArgumentException ex) {
        AccountResponseDTO response = AccountResponseDTO.builder()
                .accountNumber(null)
                .message(ex.getMessage())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .timestamp(Instant.now())
                .additionalData(null)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<AccountResponseDTO> handleGenericException(Exception ex) {
        AccountResponseDTO response = AccountResponseDTO.builder()
                .accountNumber(null)
                .message("An unexpected error occurred: " + ex.getMessage())
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .timestamp(Instant.now())
                .additionalData(null)
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
