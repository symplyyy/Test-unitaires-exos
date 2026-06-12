package com.ynov.tickets.controller;

import com.ynov.tickets.dto.CreateTicketRequest;
import com.ynov.tickets.dto.UpdateStatusRequest;
import com.ynov.tickets.model.Ticket;
import com.ynov.tickets.service.TicketService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private final TicketService service;

    public TicketController(TicketService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Ticket> create(@RequestBody CreateTicketRequest request) {
        Ticket created = service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public Ticket getOne(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping
    public List<Ticket> getAll() {
        return service.getAll();
    }

    @PatchMapping("/{id}/status")
    public Ticket updateStatus(@PathVariable Long id, @RequestBody UpdateStatusRequest request) {
        return service.updateStatus(id, request.getStatus());
    }
}
