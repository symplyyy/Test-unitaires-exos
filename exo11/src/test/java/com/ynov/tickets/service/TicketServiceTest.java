package com.ynov.tickets.service;

import com.ynov.tickets.dto.CreateTicketRequest;
import com.ynov.tickets.exception.InvalidStatusTransitionException;
import com.ynov.tickets.exception.TicketNotFoundException;
import com.ynov.tickets.exception.ValidationException;
import com.ynov.tickets.model.Priority;
import com.ynov.tickets.model.Status;
import com.ynov.tickets.model.Ticket;
import com.ynov.tickets.repository.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

    @Mock
    private TicketRepository repository;

    @InjectMocks
    private TicketService service;

    private CreateTicketRequest request;

    @BeforeEach
    void setUp() {
        request = new CreateTicketRequest();
        request.setTitle("Imprimante en panne");
        request.setPriority(Priority.HIGH);
    }

    @Test
    void create_returnsTicketWithGivenData() {
        when(repository.save(any(Ticket.class))).thenAnswer(invocation -> {
            Ticket t = invocation.getArgument(0);
            t.setId(1L);
            return t;
        });

        Ticket result = service.create(request);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getTitle()).isEqualTo("Imprimante en panne");
        assertThat(result.getPriority()).isEqualTo(Priority.HIGH);
    }

    @Test
    void create_setsStatusToOpenByDefault() {
        when(repository.save(any(Ticket.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Ticket result = service.create(request);

        assertThat(result.getStatus()).isEqualTo(Status.OPEN);
    }

    @Test
    void create_throwsWhenTitleIsNull() {
        request.setTitle(null);

        assertThatThrownBy(() -> service.create(request))
                .isInstanceOf(ValidationException.class);

        verify(repository, never()).save(any());
    }

    @Test
    void create_throwsWhenTitleTooShort() {
        request.setTitle("  a ");

        assertThatThrownBy(() -> service.create(request))
                .isInstanceOf(ValidationException.class);

        verify(repository, never()).save(any());
    }

    @Test
    void create_throwsWhenPriorityIsNull() {
        request.setPriority(null);

        assertThatThrownBy(() -> service.create(request))
                .isInstanceOf(ValidationException.class);

        verify(repository, never()).save(any());
    }

    @Test
    void getById_returnsTicketWhenItExists() {
        Ticket ticket = new Ticket("Souris HS", Priority.LOW, Status.OPEN);
        ticket.setId(5L);
        when(repository.findById(5L)).thenReturn(Optional.of(ticket));

        Ticket result = service.getById(5L);

        assertThat(result.getId()).isEqualTo(5L);
        assertThat(result.getTitle()).isEqualTo("Souris HS");
    }

    @Test
    void getById_throwsWhenTicketDoesNotExist() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getById(99L))
                .isInstanceOf(TicketNotFoundException.class);
    }

    @Test
    void getAll_returnsAllTickets() {
        Ticket t1 = new Ticket("A", Priority.LOW, Status.OPEN);
        Ticket t2 = new Ticket("B", Priority.HIGH, Status.OPEN);
        when(repository.findAll()).thenReturn(List.of(t1, t2));

        List<Ticket> result = service.getAll();

        assertThat(result).hasSize(2);
    }

    @Test
    void updateStatus_allowsOpenToInProgress() {
        Ticket ticket = existingTicket(1L, Status.OPEN);
        when(repository.findById(1L)).thenReturn(Optional.of(ticket));
        when(repository.save(any(Ticket.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Ticket result = service.updateStatus(1L, Status.IN_PROGRESS);

        assertThat(result.getStatus()).isEqualTo(Status.IN_PROGRESS);
    }

    @Test
    void updateStatus_allowsOpenToResolved() {
        Ticket ticket = existingTicket(1L, Status.OPEN);
        when(repository.findById(1L)).thenReturn(Optional.of(ticket));
        when(repository.save(any(Ticket.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Ticket result = service.updateStatus(1L, Status.RESOLVED);

        assertThat(result.getStatus()).isEqualTo(Status.RESOLVED);
    }

    @Test
    void updateStatus_allowsInProgressToResolved() {
        Ticket ticket = existingTicket(1L, Status.IN_PROGRESS);
        when(repository.findById(1L)).thenReturn(Optional.of(ticket));
        when(repository.save(any(Ticket.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Ticket result = service.updateStatus(1L, Status.RESOLVED);

        assertThat(result.getStatus()).isEqualTo(Status.RESOLVED);
    }

    @Test
    void updateStatus_refusesChangeOnResolvedTicket() {
        Ticket ticket = existingTicket(1L, Status.RESOLVED);
        when(repository.findById(1L)).thenReturn(Optional.of(ticket));

        assertThatThrownBy(() -> service.updateStatus(1L, Status.IN_PROGRESS))
                .isInstanceOf(InvalidStatusTransitionException.class);

        verify(repository, never()).save(any());
    }

    @Test
    void updateStatus_refusesForbiddenTransition() {
        Ticket ticket = existingTicket(1L, Status.IN_PROGRESS);
        when(repository.findById(1L)).thenReturn(Optional.of(ticket));

        assertThatThrownBy(() -> service.updateStatus(1L, Status.OPEN))
                .isInstanceOf(InvalidStatusTransitionException.class);

        verify(repository, never()).save(any());
    }

    @Test
    void updateStatus_throwsWhenTicketDoesNotExist() {
        when(repository.findById(42L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.updateStatus(42L, Status.IN_PROGRESS))
                .isInstanceOf(TicketNotFoundException.class);
    }

    private Ticket existingTicket(Long id, Status status) {
        Ticket ticket = new Ticket("Probleme reseau", Priority.MEDIUM, status);
        ticket.setId(id);
        return ticket;
    }
}
