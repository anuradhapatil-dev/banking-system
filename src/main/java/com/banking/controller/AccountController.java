package com.banking.controller;

import com.banking.dto.AccountInfoResponseDTO;
import com.banking.dto.AccountRequestDTO;
import com.banking.dto.AccountResponseDTO;
import com.banking.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/accounts")
    public ResponseEntity<AccountResponseDTO> createAccount(@RequestBody @Valid AccountRequestDTO request) {
        AccountResponseDTO response = accountService.createAccount(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/accounts/{accountNumber}")
    public ResponseEntity<AccountInfoResponseDTO> getAccount(@PathVariable String accountNumber) {
        AccountInfoResponseDTO accountInfo = accountService.getAccountDetails(accountNumber);
        return ResponseEntity.ok(accountInfo);
    }

}
