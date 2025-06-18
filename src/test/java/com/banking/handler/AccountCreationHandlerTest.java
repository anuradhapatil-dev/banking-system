package com.banking.handler;

import com.banking.dto.AccountRequestDTO;
import com.banking.dto.AccountResponseDTO;
import com.banking.exception.AccountAlreadyExistsException;
import com.banking.exception.IdempotentRequestException;
import com.banking.generator.AccountNumberGenerator;
import com.banking.model.Account;
import com.banking.model.Customer;
import com.banking.model.enums.AccountType;
import com.banking.repository.AccountRepository;
import com.banking.repository.CustomerRepository;
import com.banking.service.IdempotencyService;
import com.banking.validator.AccountRequestValidator;
import com.banking.validator.PasswordValidator;
import com.banking.validator.PhoneNumberValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AccountCreationHandlerTest {

    @InjectMocks
    private AccountCreationHandler handler;

    @Mock private AccountRepository accountRepository;
    @Mock private CustomerRepository customerRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private PhoneNumberValidator phoneNumberValidator;
    @Mock private PasswordValidator passwordValidator;
    @Mock private AccountNumberGenerator accountNumberGenerator;
    @Mock private AccountRequestValidator accountRequestValidator;
    @Mock
    private IdempotencyService idempotencyService;

    @BeforeEach
    void setup() {
        accountRepository = mock(AccountRepository.class);
        customerRepository = mock(CustomerRepository.class);
        accountRequestValidator = mock(AccountRequestValidator.class);
        passwordValidator = mock(PasswordValidator.class);
        phoneNumberValidator = mock(PhoneNumberValidator.class);
        accountNumberGenerator = mock(AccountNumberGenerator.class);
        idempotencyService = mock(IdempotencyService.class);
        passwordEncoder = mock(PasswordEncoder.class); // ✅ add this line

        handler = new AccountCreationHandler();
        ReflectionTestUtils.setField(handler, "accountRepository", accountRepository);
        ReflectionTestUtils.setField(handler, "customerRepository", customerRepository);
        ReflectionTestUtils.setField(handler, "passwordEncoder", passwordEncoder); // now not null
        ReflectionTestUtils.setField(handler, "phoneNumberValidator", phoneNumberValidator);
        ReflectionTestUtils.setField(handler, "passwordValidator", passwordValidator);
        ReflectionTestUtils.setField(handler, "accountNumberGenerator", accountNumberGenerator);
        ReflectionTestUtils.setField(handler, "accountRequestValidator", accountRequestValidator);
        ReflectionTestUtils.setField(handler, "idempotencyService", idempotencyService);
    }


    @Test
    @DisplayName("Should create account when valid input is provided")
    void shouldCreateAccountSuccessfully() {
        AccountRequestDTO request = new AccountRequestDTO();
        request.setPhone("9999999999");
        request.setType(AccountType.SAVINGS);
        request.setName("Anu");
        request.setPassword("pass");
        request.setInitialDeposit(BigDecimal.valueOf(1000));
        request.setIdempotencyKey("key123");

        when(accountRepository.findByCustomer_PhoneAndType(anyString(), any()))
                .thenReturn(Optional.empty());

        when(idempotencyService.isDuplicate("key123")).thenReturn(false);
        when(customerRepository.findByPhone("9999999999")).thenReturn(Optional.empty());
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
        when(customerRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);
        when(accountNumberGenerator.generate()).thenReturn("ACC001");
        when(accountRepository.existsByAccountNumber("ACC001")).thenReturn(false);
        when(accountRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        AccountResponseDTO response = handler.handle(request);

        assertEquals("ACC001", response.getAccountNumber());
        assertEquals("Account created successfully", response.getMessage());
    }

    @Test
    @DisplayName("handle - Should throw AccountAlreadyExistsException when account already exists for phone and type")
    void shouldThrowWhenDuplicateAccountExists() {
        AccountRequestDTO request = new AccountRequestDTO("Anu", "9999999999", "pass",  AccountType.SAVINGS,BigDecimal.valueOf(500), "key");

        when(accountRepository.findByCustomer_PhoneAndType("9999999999", AccountType.SAVINGS))
                .thenReturn(Optional.of(mock(Account.class)));

        assertThrows(AccountAlreadyExistsException.class, () -> handler.handle(request));
    }

    @Test
    @DisplayName("handle - Should throw IdempotentRequestException when idempotency key is duplicate")
    void shouldThrowWhenIdempotencyKeyIsDuplicate() {
        AccountRequestDTO request = new AccountRequestDTO("Anu", "9999999999", "pass",  AccountType.SAVINGS,BigDecimal.valueOf(500), "key");

        when(accountRepository.findByCustomer_PhoneAndType(any(), any())).thenReturn(Optional.empty());
        when(idempotencyService.isDuplicate("key")).thenReturn(true);

        assertThrows(IdempotentRequestException.class, () -> handler.handle(request));
    }

    @Test
    @DisplayName("handle - Should reuse existing customer when phone number already exists")
    void shouldReuseExistingCustomer() {
        AccountRequestDTO request = new AccountRequestDTO("Anu", "9999999999", "pass", AccountType.SAVINGS,  BigDecimal.valueOf(500),"key");

        Customer existingCustomer = Customer.builder().id(UUID.randomUUID()).name("Anu").phone("9999999999").password("encoded").build();

        when(accountRepository.findByCustomer_PhoneAndType(any(), any())).thenReturn(Optional.empty());
        when(idempotencyService.isDuplicate("key")).thenReturn(false);
        doNothing().when(idempotencyService).save("key");
        when(customerRepository.findByPhone("9999999999")).thenReturn(Optional.of(existingCustomer));
        when(accountNumberGenerator.generate()).thenReturn("ACC1002");
        when(accountRepository.existsByAccountNumber("ACC1002")).thenReturn(false);
        when(accountRepository.save(any(Account.class))).thenReturn(mock(Account.class));

        AccountResponseDTO response = handler.handle(request);

        assertEquals("ACC1002", response.getAccountNumber());
    }
}

