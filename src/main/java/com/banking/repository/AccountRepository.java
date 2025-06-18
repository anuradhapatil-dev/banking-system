package com.banking.repository;

import com.banking.model.Account;
import com.banking.model.enums.AccountType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {

    Optional<Account> findByCustomer_Phone(String phone);
    Optional<Account> findByAccountNumber(String accountNumber);
    boolean existsByAccountNumber(String accountNumber);

    Optional<Account> findByCustomer_PhoneAndType(String phone, AccountType type);
}
