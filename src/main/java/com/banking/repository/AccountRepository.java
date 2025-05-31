package com.banking.repository;

import com.banking.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {
    Optional<Account> findByPhone(String phone);
    boolean existsByPhone(String phone);
    boolean existsByAccountNumber(String accountNumber);
}
