package com.banking.repository;

import com.banking.model.Account;
import com.banking.model.Customer;
import com.banking.model.enums.AccountType;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AccountRepositoryTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @BeforeEach
    void setup() {
        accountRepository.deleteAll();
        customerRepository.deleteAll();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("Should return account by account number")
    void findByAccountNumber_ShouldReturnAccount() {
        // Arrange
        Customer customer = Customer.builder()
                .name("Anu")
                .phone("9999999998")
                .password("password")
                .build();
        entityManager.persist(customer);

        Account account = Account.builder()
                .accountNumber("ACC123456")
                .balance(BigDecimal.valueOf(1000))
                .type(AccountType.SAVINGS)
                .customer(customer)
                .build();
        entityManager.persist(account);
        entityManager.flush();

        // Act
        Optional<Account> result = accountRepository.findByAccountNumber("ACC123456");

        // Assert
        assertTrue(result.isPresent(), "Account should be found");
        assertEquals("ACC123456", result.get().getAccountNumber(), "Account number should match");
    }

    @Test
    @DisplayName("Should return empty if account not found")
    void findByAccountNumber_ShouldReturnEmpty() {
        Optional<Account> result = accountRepository.findByAccountNumber("NON_EXISTENT");
        assertTrue(result.isEmpty(), "Account should not be found");
    }


    @AfterEach
    void cleanup() {
        entityManager.createNativeQuery("TRUNCATE TABLE accounts, customer CASCADE").executeUpdate();
    }


}
