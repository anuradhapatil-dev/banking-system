package com.banking.service.impl;

import com.banking.dto.AccountRequestDTO;
import com.banking.dto.AccountResponseDTO;
import com.banking.exception.AccountAlreadyExistsException;
import com.banking.generator.AccountNumberGenerator;
import com.banking.model.Account;
import com.banking.model.AccountType;
import com.banking.repository.AccountRepository;
import com.banking.validator.AccountRequestValidator;
import com.banking.validator.PasswordValidator;
import com.banking.validator.PhoneNumberValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private PhoneNumberValidator phoneNumberValidator;

    @Mock
    private PasswordValidator passwordValidator;

    @Mock
    private AccountNumberGenerator accountNumberGenerator;

    @Spy
    private AccountRequestValidator accountRequestValidator;

    @InjectMocks
    private AccountServiceImpl accountService;

    private final String validPhone = "9876543210";
    private final String validPassword = "Password122!";

    @BeforeEach
    void setupMocks() {
        when(phoneNumberValidator.isValid(validPhone)).thenReturn(true);
        when(passwordValidator.isValid(validPassword)).thenReturn(true);
    }

    private AccountRequestDTO createValidRequest() {
        return new AccountRequestDTO( "Swati", validPhone, validPassword, AccountType.SAVINGS,new BigDecimal("1000.00"));
    }

    @Test
    @DisplayName("Should create account when input is valid")
    void createAccount_shouldReturnResponseDTO_whenInputIsValid() {
        AccountRequestDTO request = new AccountRequestDTO("Swati", "9876543210", "Password122!", AccountType.SAVINGS,new BigDecimal("1000.00"));

        when(phoneNumberValidator.isValid("9876543210")).thenReturn(true);
        when(passwordValidator.isValid("Password122!")).thenReturn(true);
        when(accountRepository.existsByAccountNumber(any())).thenReturn(false);
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
        when(accountNumberGenerator.generate()).thenReturn("ACC12345");

        Account savedAccount = Account.builder()
                .id(UUID.randomUUID())
                .name("Swati")
                .phone("9876543210")
                .password("encodedPassword")
                .accountNumber("ACC12345")
                .type(AccountType.SAVINGS)
                .balance(BigDecimal.ZERO)
                .build();

        when(accountRepository.save(any())).thenReturn(savedAccount);

        AccountResponseDTO response = accountService.createAccount(request);

        assertThat(response).isNotNull();
        assertThat(response.getAccountNumber()).startsWith("ACC");
        assertThat(response.getMessage()).isEqualTo("Account created successfully");

        verify(accountRepository).save(any());
    }

    @Test
    @DisplayName("Should throw exception for invalid phone number")
    void shouldThrowException_whenPhoneNumberInvalid() {
        String invalidPhone = "123";
        AccountRequestDTO request = new AccountRequestDTO( "Swati", invalidPhone, validPassword, AccountType.SAVINGS,new BigDecimal("1000.00"));

        when(phoneNumberValidator.isValid(invalidPhone)).thenReturn(false);

        assertThatThrownBy(() -> accountService.createAccount(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid phone number format");

        verify(phoneNumberValidator).isValid(invalidPhone);
        verify(passwordValidator, never()).isValid(any());
    }

    @Test
    @DisplayName("Should throw exception for weak password")
    void shouldThrowException_whenPasswordIsWeak() {
        String weakPassword = "weakpass";
        AccountRequestDTO request = new AccountRequestDTO( "Swati", validPhone, weakPassword, AccountType.SAVINGS,new BigDecimal("1000.00"));

        //when(passwordValidator.isValid("weakpass")).thenReturn(false);

        assertThatThrownBy(() -> accountService.createAccount(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Password must be at least 8 characters long");

        verify(phoneNumberValidator).isValid(validPhone);
        verify(passwordValidator).isValid(weakPassword);
    }

    @Test
    @DisplayName("Should throw exception when required fields are missing")
    void shouldThrowException_whenFieldsAreMissing() {
        AccountRequestDTO request = new AccountRequestDTO(null, null, null, null, null);

        assertThatThrownBy(() -> accountRequestValidator.validate(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Name is required");
    }

    @Test
    @DisplayName("Should throw exception when balance is negative")
    void shouldThrowException_whenBalanceIsNegative() {
        AccountRequestDTO request = new AccountRequestDTO("Anu", "9876543210", "pass", AccountType.SAVINGS, new BigDecimal("-100"));

        assertThatThrownBy(() -> accountRequestValidator.validate(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Balance cannot be negative");
    }

    @Test
    @DisplayName("Should generate account number when account number is null")
    void shouldGenerateAccountNumber_whenAccountNumberIsNull() {
        AccountRequestDTO request = new AccountRequestDTO( "Swati", validPhone, validPassword, AccountType.SAVINGS,new BigDecimal("1000.00"));

        when(accountNumberGenerator.generate()).thenReturn("ACC12345");

        Account savedAccount = Account.builder()
                .id(UUID.randomUUID())
                .name("Swati")
                .phone(validPhone)
                .password("encodedPassword")
                .accountNumber("ACC12345")
                .type(AccountType.SAVINGS)
                .balance(BigDecimal.ZERO)
                .build();

        when(accountRepository.save(any())).thenReturn(savedAccount);

        AccountResponseDTO response = accountService.createAccount(request);

        assertThat(response.getAccountNumber()).isEqualTo("ACC12345");
        verify(accountRepository).save(any());
    }

    @ParameterizedTest
    @ValueSource(strings = {"Password123!", "MyS3cureP@ssw0rd", "Str0ngPassword!"})
    @DisplayName("Should pass for valid passwords")
    void shouldPassForValidPasswords(String validPassword) {
        AccountRequestDTO request = new AccountRequestDTO( "Swati", validPhone, validPassword, AccountType.SAVINGS,new BigDecimal("1000.00"));

        when(passwordValidator.isValid(validPassword)).thenReturn(true);
        when(phoneNumberValidator.isValid(validPhone)).thenReturn(true);
        when(accountRepository.existsByAccountNumber(any())).thenReturn(false);
        when(passwordEncoder.encode(validPassword)).thenReturn("encodedPassword");
        when(accountNumberGenerator.generate()).thenReturn("ACC12345");

        Account savedAccount = Account.builder()
                .id(UUID.randomUUID())
                .name("Swati")
                .phone(validPhone)
                .password("encodedPassword")
                .accountNumber("ACC12345")
                .type(AccountType.SAVINGS)
                .balance(BigDecimal.ZERO)
                .build();

        when(accountRepository.save(any())).thenReturn(savedAccount);

        AccountResponseDTO response = accountService.createAccount(request);

        assertThat(response.getAccountNumber()).startsWith("ACC");
        assertThat(response.getMessage()).isEqualTo("Account created successfully");

        verify(accountRepository).save(any());
    }

    @ParameterizedTest
    @ValueSource(strings = {"9876543210", "1234567890", "5555555555"})
    @DisplayName("Should pass for valid phone numbers")
    void shouldPassForValidPhoneNumbers(String validPhone) {
        AccountRequestDTO request = new AccountRequestDTO("Swati", validPhone, validPassword, AccountType.SAVINGS,new BigDecimal("1000.00"));

        when(phoneNumberValidator.isValid(validPhone)).thenReturn(true);
        when(passwordValidator.isValid(validPassword)).thenReturn(true);
        when(accountRepository.existsByAccountNumber(any())).thenReturn(false);
        when(passwordEncoder.encode(validPassword)).thenReturn("encodedPassword");
        when(accountNumberGenerator.generate()).thenReturn("ACC12345");

        Account savedAccount = Account.builder()
                .id(UUID.randomUUID())
                .name("Swati")
                .phone(validPhone)
                .password("encodedPassword")
                .accountNumber("ACC12345")
                .type(AccountType.SAVINGS)
                .balance(BigDecimal.ZERO)
                .build();

        when(accountRepository.save(any())).thenReturn(savedAccount);

        AccountResponseDTO response = accountService.createAccount(request);

        assertThat(response.getAccountNumber()).startsWith("ACC");
        assertThat(response.getMessage()).isEqualTo("Account created successfully");

        verify(accountRepository).save(any());
    }

}
