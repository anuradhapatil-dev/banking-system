package com.banking.dto;

import com.banking.model.enums.AccountType;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountRequestDTO {

    @NotBlank
    private String name;

    @NotBlank
    private String phone;

    @NotBlank
    private String password;

    @NotNull
    private AccountType type;

    @NotNull
    @PositiveOrZero(message = "Balance must be zero or positive")
    private BigDecimal initialDeposit;

    private String idempotencyKey;
}
