package com.banking.service.impl;

import com.banking.dto.AccountInfoResponseDTO;
import com.banking.dto.AccountRequestDTO;
import com.banking.dto.AccountResponseDTO;
import com.banking.handler.AccountCreationHandler;
import com.banking.handler.AccountRetrievalHandler;
import com.banking.model.enums.AccountType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class AccountServiceImplTest {

    @Mock
    private AccountCreationHandler accountCreationHandler;

    @Mock
    private AccountRetrievalHandler accountRetrievalHandler;

    @InjectMocks
    private AccountServiceImpl accountService;

    @BeforeEach
    void setup() {
        openMocks(this);
    }

    @Test
    @DisplayName("createAccount - Should delegate to AccountCreationHandler and return AccountResponseDTO")
    void createAccount_shouldDelegateToHandlerAndReturnResponse() {
        AccountRequestDTO request = new AccountRequestDTO(
                "Anu", "9999999999", "pass", AccountType.SAVINGS, BigDecimal.valueOf(1000), "idempotent-key"
        );

        AccountResponseDTO expectedResponse = new AccountResponseDTO(
                "ACC001", "Account created successfully", 201, Instant.now(), null
        );

        when(accountCreationHandler.handle(request)).thenReturn(expectedResponse);

        AccountResponseDTO response = accountService.createAccount(request);

        assertEquals("ACC001", response.getAccountNumber());
        assertEquals("Account created successfully", response.getMessage());
        verify(accountCreationHandler).handle(request);
    }

    @Test
    @DisplayName("getAccountDetails - Should delegate to AccountRetrievalHandler and return AccountInfoResponseDTO")
    void getAccountDetails_shouldDelegateToHandlerAndReturnInfo() {
        String accountNumber = "ACC001";

        AccountInfoResponseDTO expectedInfo = AccountInfoResponseDTO.builder()
                .accountNumber(accountNumber)
                .customerName("Anu")
                .balance(BigDecimal.valueOf(1000))
                .accountType(AccountType.SAVINGS)
                .phone("9999999999")
                .timestamp(Instant.now())
                .build();

        when(accountRetrievalHandler.handle(accountNumber)).thenReturn(expectedInfo);

        AccountInfoResponseDTO result = accountService.getAccountDetails(accountNumber);

        assertNotNull(result);
        assertEquals("ACC001", result.getAccountNumber());
        verify(accountRetrievalHandler).handle(accountNumber);
    }
}
