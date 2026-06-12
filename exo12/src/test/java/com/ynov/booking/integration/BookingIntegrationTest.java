package com.ynov.booking.integration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BookingIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void fullFlowCreateRoomReserveGetCancel() throws Exception {
        MvcResult roomResult = mockMvc.perform(post("/api/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Salle A\",\"capacity\":10}"))
                .andExpect(status().isCreated())
                .andReturn();

        long roomId = objectMapper.readTree(roomResult.getResponse().getContentAsString()).get("id").asLong();

        MvcResult reservationResult = mockMvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"roomId\":" + roomId + ",\"personName\":\"Timeo\",\"start\":\"2026-06-12T10:00:00\",\"end\":\"2026-06-12T11:00:00\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("CONFIRMED"))
                .andReturn();

        long reservationId = objectMapper.readTree(reservationResult.getResponse().getContentAsString()).get("id").asLong();

        mockMvc.perform(get("/api/reservations/" + reservationId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.personName").value("Timeo"));

        MvcResult cancelResult = mockMvc.perform(patch("/api/reservations/" + reservationId + "/cancel"))
                .andExpect(status().isOk())
                .andReturn();

        JsonNode cancelled = objectMapper.readTree(cancelResult.getResponse().getContentAsString());
        assertThat(cancelled.get("status").asText()).isEqualTo("CANCELLED");
    }
}
