package com.banking.validator;

import com.banking.dto.AccountRequestDTO;
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
        if (request.getInitialDeposit() == null) {
            throw new IllegalArgumentException("Initial balance is required");
        }
        if (request.getInitialDeposit().signum() < 0) {
            throw new IllegalArgumentException("Initial balance cannot be negative");
        }
    }
}
