package com.banking.dto;

import com.banking.model.enums.AccountType;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountInfoResponseDTO {
    private String accountNumber;
    private String customerName;
    private String phone;
    private AccountType accountType;
    private BigDecimal balance;
    private Instant timestamp;
}
