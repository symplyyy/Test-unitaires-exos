package com.ynov.tickets.service;

import com.ynov.tickets.dto.CreateTicketRequest;
import com.ynov.tickets.model.Status;
import com.ynov.tickets.model.Ticket;
import com.ynov.tickets.repository.TicketRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketService {

    private final TicketRepository repository;

    public TicketService(TicketRepository repository) {
        this.repository = repository;
    }

    public Ticket create(CreateTicketRequest request) {
        throw new UnsupportedOperationException("pas encore implémenté");
    }

    public Ticket getById(Long id) {
        throw new UnsupportedOperationException("pas encore implémenté");
    }

    public List<Ticket> getAll() {
        throw new UnsupportedOperationException("pas encore implémenté");
    }

    public Ticket updateStatus(Long id, Status newStatus) {
        throw new UnsupportedOperationException("pas encore implémenté");
    }
}
