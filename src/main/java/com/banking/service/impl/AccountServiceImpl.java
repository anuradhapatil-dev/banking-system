package com.banking.service.impl;

import com.banking.dto.AccountInfoResponseDTO;
import com.banking.dto.AccountRequestDTO;
import com.banking.dto.AccountResponseDTO;
import com.banking.exception.AccountAlreadyExistsException;
import com.banking.exception.IdempotentRequestException;
import com.banking.exception.ResourceNotFoundException;
import com.banking.handler.AccountCreationHandler;
import com.banking.handler.AccountRetrievalHandler;
import com.banking.model.Customer;
import com.banking.model.Account;
import com.banking.repository.AccountRepository;
import com.banking.repository.CustomerRepository;
import com.banking.service.AccountService;
import com.banking.service.IdempotencyService;
import com.banking.validator.AccountRequestValidator;
import com.banking.validator.PhoneNumberValidator;
import com.banking.validator.PasswordValidator;
import com.banking.generator.AccountNumberGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountCreationHandler accountCreationHandler;
    private final AccountRetrievalHandler accountRetrievalHandler;

    @Override
    @Transactional
    public AccountResponseDTO createAccount(AccountRequestDTO request) {
        return accountCreationHandler.handle(request);
    }

    @Override
    public AccountInfoResponseDTO getAccountDetails(String accountNumber) {
        return accountRetrievalHandler.handle(accountNumber);
    }
}
