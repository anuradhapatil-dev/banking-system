package com.banking.service;

import com.banking.dto.AccountInfoResponseDTO;
import com.banking.dto.AccountRequestDTO;
import com.banking.dto.AccountResponseDTO;

public interface AccountService {
    AccountResponseDTO createAccount(AccountRequestDTO requestDto);
    AccountInfoResponseDTO getAccountDetails(String accountNumber);
}
