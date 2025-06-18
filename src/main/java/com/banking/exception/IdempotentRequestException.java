package com.banking.exception;

public class IdempotentRequestException extends RuntimeException {
    public IdempotentRequestException(String message) {
        super(message);
    }

}
