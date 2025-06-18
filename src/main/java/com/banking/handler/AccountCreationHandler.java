package com.banking.handler;

import com.banking.dto.AccountRequestDTO;
import com.banking.dto.AccountResponseDTO;
import com.banking.exception.AccountAlreadyExistsException;
import com.banking.exception.IdempotentRequestException;
import com.banking.generator.AccountNumberGenerator;
import com.banking.model.Account;
import com.banking.model.Customer;
import com.banking.repository.AccountRepository;
import com.banking.repository.CustomerRepository;
import com.banking.service.IdempotencyService;
import com.banking.validator.AccountRequestValidator;
import com.banking.validator.PasswordValidator;
import com.banking.validator.PhoneNumberValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

@Slf4j
@Component
public class AccountCreationHandler {

    @Autowired private AccountRepository accountRepository;
    @Autowired private CustomerRepository customerRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private PhoneNumberValidator phoneNumberValidator;
    @Autowired private PasswordValidator passwordValidator;
    @Autowired private AccountNumberGenerator accountNumberGenerator;
    @Autowired private AccountRequestValidator accountRequestValidator;
    @Autowired private IdempotencyService idempotencyService;

    @Transactional
    public AccountResponseDTO handle(AccountRequestDTO request) {
        accountRequestValidator.validate(request);

        Optional<Account> existing = accountRepository
                .findByCustomer_PhoneAndType(request.getPhone(), request.getType());

        if (existing.isPresent()) {
            throw new AccountAlreadyExistsException("Account already exists for this phone and type.");
        }

        if (idempotencyService.isDuplicate(request.getIdempotencyKey())) {
            throw new IdempotentRequestException("Duplicate request.");
        }

        idempotencyService.save(request.getIdempotencyKey());

        Customer customer = customerRepository.findByPhone(request.getPhone())
                .orElseGet(() -> {
                    Customer newCustomer = Customer.builder()
                            .name(request.getName())
                            .phone(request.getPhone())
                            .password(passwordEncoder.encode(request.getPassword()))
                            .build();
                    return customerRepository.save(newCustomer);
                });

        String accountNumber;
        do {
            accountNumber = accountNumberGenerator.generate();
        } while (accountRepository.existsByAccountNumber(accountNumber));

        Account account = Account.builder()
                .accountNumber(accountNumber)
                .balance(request.getInitialDeposit())
                .type(request.getType())
                .customer(customer)
                .build();

        accountRepository.save(account);
        log.info("Account created: {}", accountNumber);

        return new AccountResponseDTO(
                accountNumber,
                "Account created successfully",
                HttpStatus.CREATED.value(),
                Instant.now(),
                null
        );
    }
}
