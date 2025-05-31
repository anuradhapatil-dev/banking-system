package com.banking.mapper;

import com.banking.dto.AccountRequestDTO;
import com.banking.model.Account;

public class AccountMapper {

    public static Account toEntity(AccountRequestDTO dto) {
        return Account.builder()
                .name(dto.getName())
                .phone(dto.getPhone())
                .password(dto.getPassword()) // Encode in Service, not here
                .type(dto.getType())
                .balance(dto.getBalance())   // ✅ Ensure balance is mapped
                .build();
    }
}
