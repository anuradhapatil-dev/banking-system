package com.banking.controller;

import com.banking.dto.AccountInfoResponseDTO;
import com.banking.dto.AccountRequestDTO;
import com.banking.dto.AccountResponseDTO;
import com.banking.model.enums.AccountType;
import com.banking.service.AccountService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.ArgumentMatchers.any;

import java.math.BigDecimal;
import java.time.Instant;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
// other imports ...

@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @Autowired
    private ObjectMapper objectMapper;  // add this to convert DTO to JSON


    @Test
    @DisplayName("POST /accounts - Should create account when valid input is provided")
    void createAccount_ValidInput_ShouldReturnCreated() throws Exception {
        AccountRequestDTO request = new AccountRequestDTO("Anu", "9999999999", "password", AccountType.SAVINGS, new BigDecimal("1000.00"),"UNIQUE-KEY-TEST-001");
        AccountResponseDTO response = new AccountResponseDTO("ACC123456", "Account created", 201, Instant.now(), null);

        when(accountService.createAccount(any())).thenReturn(response);

        mockMvc.perform(post("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.accountNumber").value("ACC123456"))
                .andExpect(jsonPath("$.message").value("Account created"));
    }

    @Test
    @DisplayName("POST /accounts - Should return 400 Bad Request for invalid input")
    void createAccount_InvalidInput_ShouldReturnBadRequest() throws Exception {
        // e.g. missing phone number
        AccountRequestDTO request = new AccountRequestDTO("Anu", null, "password", AccountType.SAVINGS, new BigDecimal("1000.00"),"UNIQUE-KEY-TEST-001");

        mockMvc.perform(post("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("getAccountDetails - Should return account details for valid account number")
    void shouldReturnAccountDetails() throws Exception {
        AccountInfoResponseDTO responseDTO = new AccountInfoResponseDTO(
                "ACC123456", "Anu", "9999999999", AccountType.SAVINGS, new BigDecimal("1000"),Instant.now()
        );

        when(accountService.getAccountDetails("ACC123456")).thenReturn(responseDTO);

        mockMvc.perform(get("/api/accounts/ACC123456"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerName").value("Anu"));
    }

}
