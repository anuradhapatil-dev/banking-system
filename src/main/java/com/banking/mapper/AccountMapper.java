package com.banking.mapper;

import com.banking.dto.AccountRequestDTO;
import com.banking.model.Customer;
import com.banking.model.Account;

public class AccountMapper {

    public static Account toEntity(AccountRequestDTO dto, Customer customer, String generatedAccountNumber) {
        System.out.println("DTO received: " + dto);
        return Account.builder()
                .customer(customer)
                .type(dto.getType())
                .balance(dto.getInitialDeposit())
                .accountNumber(generatedAccountNumber)
                .build();
    }
}
