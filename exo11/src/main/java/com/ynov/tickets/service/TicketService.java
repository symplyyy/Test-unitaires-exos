package com.ynov.tickets.service;

import com.ynov.tickets.dto.CreateTicketRequest;
import com.ynov.tickets.exception.InvalidStatusTransitionException;
import com.ynov.tickets.exception.TicketNotFoundException;
import com.ynov.tickets.exception.ValidationException;
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
        if (request.getTitle() == null || request.getTitle().trim().length() < 3) {
            throw new ValidationException("Le titre doit contenir au moins 3 caracteres");
        }
        if (request.getPriority() == null) {
            throw new ValidationException("La priorite est obligatoire");
        }

        Ticket ticket = new Ticket(request.getTitle(), request.getPriority(), Status.OPEN);
        return repository.save(ticket);
    }

    public Ticket getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new TicketNotFoundException(id));
    }

    public List<Ticket> getAll() {
        return repository.findAll();
    }

    public Ticket updateStatus(Long id, Status newStatus) {
        Ticket ticket = getById(id);

        if (!isTransitionAllowed(ticket.getStatus(), newStatus)) {
            throw new InvalidStatusTransitionException(ticket.getStatus(), newStatus);
        }

        ticket.setStatus(newStatus);
        return repository.save(ticket);
    }

    private boolean isTransitionAllowed(Status current, Status target) {
        if (current == Status.OPEN) {
            return target == Status.IN_PROGRESS || target == Status.RESOLVED;
        }
        if (current == Status.IN_PROGRESS) {
            return target == Status.RESOLVED;
        }
        return false;
    }
}
