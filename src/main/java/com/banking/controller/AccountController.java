package com.banking.controller;

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
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<AccountResponseDTO> createAccount(@Valid @RequestBody AccountRequestDTO requestDTO) {
        AccountResponseDTO response = accountService.createAccount(requestDTO);

        // Add statusCode and timestamp here if not set inside service
        response.setStatusCode(HttpStatus.CREATED.value());
        response.setTimestamp(Instant.now());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
