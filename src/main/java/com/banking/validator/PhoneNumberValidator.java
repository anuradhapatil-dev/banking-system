package com.banking.validator;

import org.springframework.stereotype.Component;

@Component
public class PhoneNumberValidator {
    public boolean isValid(String phone) {
        if (phone == null) return false;
        // Remove spaces, dashes, parentheses
        String normalized = phone.replaceAll("[\\s\\-()]", "");
        // Accept 10 digits or +1 followed by 10 digits
        return normalized.matches("(\\+1)?\\d{10}");
    }

}
