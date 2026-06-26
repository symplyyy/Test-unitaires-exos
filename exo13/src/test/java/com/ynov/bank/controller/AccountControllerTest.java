package com.ynov.bank.controller;

import com.ynov.bank.exception.AccountAlreadyExistsException;
import com.ynov.bank.exception.AccountNotFoundException;
import com.ynov.bank.exception.InsufficientFundsException;
import com.ynov.bank.exception.InvalidAmountException;
import com.ynov.bank.model.Account;
import com.ynov.bank.service.AccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// Ici on teste la couche web : le service est mocke, on verifie juste
// les routes, les codes HTTP et le JSON renvoye.
@WebMvcTest(AccountController.class)
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService service;

    @Test
    void createAccountReturns201() throws Exception {
        when(service.create(any())).thenReturn(account("ACC-1", "Alice", "0"));

        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"number\":\"ACC-1\",\"owner\":\"Alice\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.number").value("ACC-1"))
                .andExpect(jsonPath("$.owner").value("Alice"))
                .andExpect(jsonPath("$.balance").value(0));
    }

    @Test
    void createAccountReturns409WhenNumberAlreadyExists() throws Exception {
        when(service.create(any())).thenThrow(new AccountAlreadyExistsException("ACC-1"));

        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"number\":\"ACC-1\",\"owner\":\"Alice\"}"))
                .andExpect(status().isConflict());
    }

    @Test
    void getAccountReturns200WhenItExists() throws Exception {
        when(service.getByNumber("ACC-1")).thenReturn(account("ACC-1", "Alice", "100"));

        mockMvc.perform(get("/accounts/ACC-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.number").value("ACC-1"))
                .andExpect(jsonPath("$.balance").value(100));
    }

    @Test
    void getAccountReturns404WhenMissing() throws Exception {
        when(service.getByNumber("UNKNOWN")).thenThrow(new AccountNotFoundException("UNKNOWN"));

        mockMvc.perform(get("/accounts/UNKNOWN"))
                .andExpect(status().isNotFound());
    }

    @Test
    void depositReturns200() throws Exception {
        when(service.deposit(eq("ACC-1"), any(BigDecimal.class))).thenReturn(account("ACC-1", "Alice", "150"));

        mockMvc.perform(post("/accounts/ACC-1/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amount\":50}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(150));
    }

    @Test
    void depositReturns400WhenAmountIsInvalid() throws Exception {
        when(service.deposit(eq("ACC-1"), any(BigDecimal.class)))
                .thenThrow(new InvalidAmountException("montant invalide"));

        mockMvc.perform(post("/accounts/ACC-1/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amount\":0}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void withdrawReturns409WhenFundsAreInsufficient() throws Exception {
        when(service.withdraw(eq("ACC-1"), any(BigDecimal.class)))
                .thenThrow(new InsufficientFundsException("ACC-1"));

        mockMvc.perform(post("/accounts/ACC-1/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amount\":1000}"))
                .andExpect(status().isConflict());
    }

    @Test
    void transferReturns200() throws Exception {
        mockMvc.perform(post("/accounts/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"from\":\"ACC-1\",\"to\":\"ACC-2\",\"amount\":50}"))
                .andExpect(status().isOk());
    }

    private Account account(String number, String owner, String balance) {
        Account account = new Account(number, owner);
        account.setBalance(new BigDecimal(balance));
        return account;
    }
}
