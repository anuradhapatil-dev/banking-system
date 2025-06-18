package com.banking.validator;

import com.banking.dto.AccountRequestDTO;
import com.banking.model.enums.AccountType;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;

public class AccountRequestValidatorTest {

    private AccountRequestValidator validator = new AccountRequestValidator();

    @Test
    void validate_ValidRequest_ShouldNotThrow() {
        AccountRequestDTO request = new AccountRequestDTO("Anu", "9999999999", "password", AccountType.SAVINGS, new BigDecimal("1000.00"),"UNIQUE-KEY-TEST-001");
        assertDoesNotThrow(() -> validator.validate(request));
    }

    @Test
    void validate_MissingPhone_ShouldThrow() {
        AccountRequestDTO request = new AccountRequestDTO("Anu", null, "password", AccountType.SAVINGS, new BigDecimal("1000.00"),"UNIQUE-KEY-TEST-001");
        Exception ex = assertThrows(IllegalArgumentException.class, () -> validator.validate(request));
        assertEquals("Phone number is required", ex.getMessage());
    }
}

