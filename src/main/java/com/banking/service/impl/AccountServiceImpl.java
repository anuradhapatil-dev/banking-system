package com.banking.service.impl;

import com.banking.dto.AccountRequestDTO;
import com.banking.dto.AccountResponseDTO;
import com.banking.exception.AccountAlreadyExistsException;
import com.banking.mapper.AccountMapper;
import com.banking.model.Account;
import com.banking.repository.AccountRepository;
import com.banking.service.AccountService;
import com.banking.validator.AccountRequestValidator;
import com.banking.validator.PhoneNumberValidator;
import com.banking.validator.PasswordValidator;
import com.banking.generator.AccountNumberGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final PhoneNumberValidator phoneNumberValidator;
    private final PasswordValidator passwordValidator;
    private final AccountNumberGenerator accountNumberGenerator;
    private final AccountRequestValidator accountRequestValidator;


    @Override
    public AccountResponseDTO createAccount(AccountRequestDTO requestDto) {

        accountRequestValidator.validate(requestDto);

        if (!phoneNumberValidator.isValid(requestDto.getPhone())) {
            throw new IllegalArgumentException("Invalid phone number format");
        }

        if (!passwordValidator.isValid(requestDto.getPassword())) {
            throw new IllegalArgumentException("Password must be at least 8 characters long, and contain one digit, one uppercase letter, and one special character");
        }

        if (accountRepository.existsByPhone(requestDto.getPhone())) {
            throw new AccountAlreadyExistsException("An account with this phone number already exists.");
        }

        Account account = AccountMapper.toEntity(requestDto);

        String accountNumber;
        do {
            accountNumber = accountNumberGenerator.generate();
        } while (accountRepository.existsByAccountNumber(accountNumber));
        account.setAccountNumber(accountNumber);

        account.setPassword(passwordEncoder.encode(requestDto.getPassword()));

        accountRepository.save(account);

        return new AccountResponseDTO(
                account.getAccountNumber(),
                "Account created successfully",
                201,
                Instant.now(),
                null
        );
    }


}
