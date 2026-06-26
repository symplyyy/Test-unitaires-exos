package com.ynov.bank.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// Test d'integration : tout le contexte Spring tourne pour de vrai.
// On deroule un scenario complet de bout en bout via HTTP.
@SpringBootTest
@AutoConfigureMockMvc
class BankIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void fullFlowCreateDepositWithdrawTransfer() throws Exception {
        // creation des deux comptes (solde 0 au depart)
        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"number\":\"IT-1\",\"owner\":\"Alice\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.balance").value(0));

        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"number\":\"IT-2\",\"owner\":\"Bob\"}"))
                .andExpect(status().isCreated());

        // depot de 200 sur IT-1
        mockMvc.perform(post("/accounts/IT-1/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amount\":200}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(200));

        // retrait de 50 -> il reste 150
        mockMvc.perform(post("/accounts/IT-1/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amount\":50}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(150));

        // virement de 100 vers IT-2
        mockMvc.perform(post("/accounts/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"from\":\"IT-1\",\"to\":\"IT-2\",\"amount\":100}"))
                .andExpect(status().isOk());

        // au final : IT-1 = 50, IT-2 = 100
        mockMvc.perform(get("/accounts/IT-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(50));

        mockMvc.perform(get("/accounts/IT-2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(100));
    }
}
