package com.banking.handler;

import com.banking.dto.AccountInfoResponseDTO;
import com.banking.exception.ResourceNotFoundException;
import com.banking.model.Account;
import com.banking.model.Customer;
import com.banking.model.enums.AccountType;
import com.banking.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AccountRetrievalHandlerTest {

    private AccountRepository accountRepository;
    private AccountRetrievalHandler handler;

    @BeforeEach
    void setup() {
        accountRepository = mock(AccountRepository.class);
        handler = new AccountRetrievalHandler(accountRepository);
    }

    @Test
    @DisplayName("getAccountDetails - Should return account details successfully when account exists")
    void shouldReturnAccountDetails() {
        Customer customer = Customer.builder()
                .id(UUID.randomUUID())
                .name("Anu")
                .phone("9999999999")
                .build();

        Account account = Account.builder()
                .accountNumber("ACC123")
                .balance(BigDecimal.valueOf(1000))
                .type(AccountType.SAVINGS)
                .customer(customer)
                .build();

        when(accountRepository.findByAccountNumber("ACC123")).thenReturn(Optional.of(account));

        AccountInfoResponseDTO response = handler.handle("ACC123");

        assertEquals("ACC123", response.getAccountNumber());
        assertEquals(BigDecimal.valueOf(1000), response.getBalance());
        assertEquals("Anu", response.getCustomerName());
    }

    @Test
    @DisplayName("getAccountDetails - Should throw ResourceNotFoundException when account not found")
    void shouldThrowWhenAccountNotFound() {
        when(accountRepository.findByAccountNumber("INVALID")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> handler.handle("INVALID"));
    }
}

