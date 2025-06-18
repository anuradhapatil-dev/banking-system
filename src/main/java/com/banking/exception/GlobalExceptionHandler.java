package com.banking.exception;

import com.banking.dto.AccountResponseDTO;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Common response builder method
    private ResponseEntity<AccountResponseDTO> buildErrorResponse(String message, HttpStatus status) {
        return ResponseEntity.status(status)
                .body(AccountResponseDTO.builder()
                        .accountNumber(null)
                        .message(message)
                        .statusCode(status.value())
                        .timestamp(Instant.now())
                        .additionalData(null)
                        .build());
    }

    @ExceptionHandler(DuplicateAccountException.class)
    public ResponseEntity<AccountResponseDTO> handleDuplicateAccount(DuplicateAccountException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(AccountAlreadyExistsException.class)
    public ResponseEntity<AccountResponseDTO> handleAccountAlreadyExists(AccountAlreadyExistsException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<AccountResponseDTO> handleNotFound(ResourceNotFoundException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<AccountResponseDTO> handleValidationException(MethodArgumentNotValidException ex) {
        StringBuilder errors = new StringBuilder();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.append(error.getField()).append(": ").append(error.getDefaultMessage()).append("; ")
        );
        return buildErrorResponse(errors.toString().trim(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<AccountResponseDTO> handleIllegalArgumentException(IllegalArgumentException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // Final fallback - catch-all
    @ExceptionHandler(Exception.class)
    public ResponseEntity<AccountResponseDTO> handleGenericException(Exception ex) {
        // Log stacktrace for debugging
        ex.printStackTrace();
        return buildErrorResponse("Unexpected error: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<AccountResponseDTO> handleConstraintViolation(DataIntegrityViolationException ex) {
        if (ex.getCause() instanceof ConstraintViolationException) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(AccountResponseDTO.builder()
                            .message("Phone number already in use.")
                            .timestamp(Instant.now())
                            .statusCode(HttpStatus.CONFLICT.value())
                            .build());
        }
        return handleGenericException(ex);
    }

}
