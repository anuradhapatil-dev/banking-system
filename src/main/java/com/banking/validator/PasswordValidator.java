package com.banking.validator;

import org.springframework.stereotype.Component;

@Component
public class PasswordValidator {

    private static final String PASSWORD_PATTERN =
            "^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";

    /**
     * Validates if the given password meets strength requirements:
     * - At least 8 characters
     * - At least one uppercase letter
     * - At least one digit
     * - At least one special character
     *
     * @param password the password to validate
     * @return true if valid, false otherwise
     */
    public boolean isValid(String password) {
        if (password == null) {
            return false;
        }
        return password.matches(PASSWORD_PATTERN);
    }
}

