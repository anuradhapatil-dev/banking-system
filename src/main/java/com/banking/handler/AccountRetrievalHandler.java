package com.banking.handler;

import com.banking.dto.AccountInfoResponseDTO;
import com.banking.exception.ResourceNotFoundException;
import com.banking.model.Account;
import com.banking.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class AccountRetrievalHandler {

    private final AccountRepository accountRepository;

    public AccountInfoResponseDTO handle(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

        return AccountInfoResponseDTO.builder()
                .accountNumber(account.getAccountNumber())
                .balance(account.getBalance())
                .accountType(account.getType())
                .customerName(account.getCustomer().getName())
                .phone(account.getCustomer().getPhone())
                .timestamp(Instant.now())
                .build();
    }
}

