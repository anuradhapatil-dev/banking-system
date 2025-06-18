package com.banking.mapper;

import com.banking.dto.AccountRequestDTO;
import com.banking.model.Account;
import com.banking.model.Customer;
import com.banking.model.enums.AccountType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;
import java.util.UUID;

public class AccountMapperTest {

    @Test
    @DisplayName("toEntity - Should correctly map all DTO fields to Account entity")
    void toEntity_ShouldMapFieldsCorrectly() {
        Customer customer = Customer.builder()
                .id(UUID.randomUUID())
                .name("Anu")
                .phone("9999999999")
                .build();

        AccountRequestDTO dto = new AccountRequestDTO("Anu", "9999999999", "password", AccountType.SAVINGS, new BigDecimal("1000.00"),"UNIQUE-KEY-TEST-001");

        String generatedAccountNumber = "ACC123456";

        Account account = AccountMapper.toEntity(dto, customer, generatedAccountNumber);

        assertEquals(customer, account.getCustomer());
        assertEquals(AccountType.SAVINGS, account.getType());
        assertEquals(new BigDecimal("1000.00"), account.getBalance());
        assertEquals(generatedAccountNumber, account.getAccountNumber());
    }
}

