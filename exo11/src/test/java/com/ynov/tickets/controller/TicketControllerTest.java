package com.ynov.tickets.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ynov.tickets.dto.CreateTicketRequest;
import com.ynov.tickets.exception.InvalidStatusTransitionException;
import com.ynov.tickets.exception.TicketNotFoundException;
import com.ynov.tickets.exception.ValidationException;
import com.ynov.tickets.model.Priority;
import com.ynov.tickets.model.Status;
import com.ynov.tickets.model.Ticket;
import com.ynov.tickets.service.TicketService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TicketController.class)
class TicketControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TicketService service;

    @Test
    void createReturns201WithTicket() throws Exception {
        CreateTicketRequest request = new CreateTicketRequest();
        request.setTitle("Ecran noir");
        request.setPriority(Priority.HIGH);

        Ticket created = new Ticket("Ecran noir", Priority.HIGH, Status.OPEN);
        created.setId(1L);
        when(service.create(any(CreateTicketRequest.class))).thenReturn(created);

        mockMvc.perform(post("/api/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Ecran noir"))
                .andExpect(jsonPath("$.status").value("OPEN"));
    }

    @Test
    void createReturns400WhenInvalid() throws Exception {
        CreateTicketRequest request = new CreateTicketRequest();
        request.setTitle("a");
        request.setPriority(Priority.LOW);

        when(service.create(any(CreateTicketRequest.class)))
                .thenThrow(new ValidationException("titre invalide"));

        mockMvc.perform(post("/api/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getOneReturns200() throws Exception {
        Ticket ticket = new Ticket("Wifi lent", Priority.MEDIUM, Status.OPEN);
        ticket.setId(3L);
        when(service.getById(3L)).thenReturn(ticket);

        mockMvc.perform(get("/api/tickets/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.title").value("Wifi lent"));
    }

    @Test
    void getOneReturns404WhenMissing() throws Exception {
        when(service.getById(99L)).thenThrow(new TicketNotFoundException(99L));

        mockMvc.perform(get("/api/tickets/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllReturns200WithList() throws Exception {
        Ticket t1 = new Ticket("A", Priority.LOW, Status.OPEN);
        t1.setId(1L);
        Ticket t2 = new Ticket("B", Priority.HIGH, Status.OPEN);
        t2.setId(2L);
        when(service.getAll()).thenReturn(List.of(t1, t2));

        mockMvc.perform(get("/api/tickets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void updateStatusReturns200() throws Exception {
        Ticket ticket = new Ticket("Maj", Priority.LOW, Status.IN_PROGRESS);
        ticket.setId(1L);
        when(service.updateStatus(eq(1L), eq(Status.IN_PROGRESS))).thenReturn(ticket);

        mockMvc.perform(patch("/api/tickets/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"IN_PROGRESS\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"));
    }

    @Test
    void updateStatusReturns409OnConflict() throws Exception {
        when(service.updateStatus(eq(1L), any(Status.class)))
                .thenThrow(new InvalidStatusTransitionException(Status.RESOLVED, Status.OPEN));

        mockMvc.perform(patch("/api/tickets/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"OPEN\"}"))
                .andExpect(status().isConflict());
    }
}
