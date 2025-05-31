package com.banking.validator;

import com.banking.dto.AccountRequestDTO;
import com.banking.model.AccountType;
import org.springframework.stereotype.Component;

@Component
public class AccountRequestValidator {
    public void validate(AccountRequestDTO request) {
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Name is required");
        }
        if (request.getPhone() == null || request.getPhone().trim().isEmpty()) {
            throw new IllegalArgumentException("Phone number is required");
        }
        if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Password is required");
        }
        if (request.getType() == null) {
            throw new IllegalArgumentException("Account type is required");
        }
        if (request.getBalance() == null) {
            throw new IllegalArgumentException("Balance is required");
        }
        if (request.getBalance().signum() < 0) {
            throw new IllegalArgumentException("Balance cannot be negative");
        }
    }
}
