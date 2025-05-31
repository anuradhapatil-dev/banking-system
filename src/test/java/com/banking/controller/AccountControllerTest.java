package com.banking.controller;

import com.banking.controller.AccountController;
import com.banking.dto.AccountRequestDTO;
import com.banking.dto.AccountResponseDTO;
import com.banking.exception.AccountAlreadyExistsException;
import com.banking.model.AccountType;
import com.banking.service.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.Instant;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AccountControllerTest {

    @Mock
    private AccountService accountService;

    @InjectMocks
    private AccountController accountController;

    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(accountController).build();
    }

    @Test
    void shouldRegisterAccountSuccessfully() throws Exception {
        AccountRequestDTO request = new AccountRequestDTO("Anuradha", "9876543210", "Password@123", AccountType.SAVINGS, new BigDecimal("1500.75"));

        when(accountService.createAccount(request))
                .thenReturn(new AccountResponseDTO("ANU89ECC", "Account created successfully", 201, Instant.now(), null));

        mockMvc.perform(post("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.accountNumber").value("ANU89ECC"))
                .andExpect(jsonPath("$.message").value("Account created successfully"));
    }

    @Test
    void shouldReturnConflictWhenAccountAlreadyExists() throws Exception {
        AccountRequestDTO request = new AccountRequestDTO("Anuradha", "9876543210", "Password@123", AccountType.SAVINGS, new BigDecimal("1500.75"));

        when(accountService.createAccount(request))
                .thenThrow(new AccountAlreadyExistsException("Account already exists"));

        mockMvc.perform(post("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Account already exists"))
                .andExpect(jsonPath("$.accountNumber").doesNotExist());
    }

    @Test
    void shouldFailWhenMissingFields() throws Exception {
        AccountRequestDTO request = new AccountRequestDTO("", "", "", null, null);

        mockMvc.perform(post("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
