package com.ynov.mediacity.service;

import com.ynov.mediacity.exception.BookAvailableException;
import com.ynov.mediacity.exception.DuplicateReservationException;
import com.ynov.mediacity.exception.MemberSuspendedException;
import com.ynov.mediacity.model.Book;
import com.ynov.mediacity.model.Loan;
import com.ynov.mediacity.model.Member;
import com.ynov.mediacity.model.Reservation;
import com.ynov.mediacity.repository.BookRepository;
import com.ynov.mediacity.repository.LoanRepository;
import com.ynov.mediacity.repository.MemberRepository;
import com.ynov.mediacity.repository.ReservationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private MemberRepository memberRepository;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private LoanRepository loanRepository;
    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private ReservationService service;

    private final LocalDateTime now = LocalDateTime.of(2026, 1, 1, 10, 0);

    @Test
    void reserve_savesReservationWhenBookIsUnavailable() {
        when(memberRepository.findById("M1")).thenReturn(Optional.of(new Member("M1", "Alice")));
        when(bookRepository.findById("B1")).thenReturn(Optional.of(new Book("B1", "Dune")));
        Loan onLoan = new Loan("M9", "B1", LocalDate.now(), LocalDate.now().plusDays(21));
        when(loanRepository.findActiveLoanForBook("B1")).thenReturn(Optional.of(onLoan));
        when(reservationRepository.findByBookInOrder("B1")).thenReturn(List.of());
        when(reservationRepository.save(any(Reservation.class))).thenAnswer(inv -> inv.getArgument(0));

        Reservation result = service.reserve("M1", "B1", now);

        assertThat(result.getMemberId()).isEqualTo("M1");
        assertThat(result.getBookId()).isEqualTo("B1");
    }

    @Test
    void reserve_throwsWhenMemberIsSuspended() {
        Member suspended = new Member("M1", "Alice");
        suspended.suspend();
        when(memberRepository.findById("M1")).thenReturn(Optional.of(suspended));

        assertThatThrownBy(() -> service.reserve("M1", "B1", now))
                .isInstanceOf(MemberSuspendedException.class);
        verify(reservationRepository, never()).save(any());
    }

    @Test
    void reserve_throwsWhenBookIsAvailable() {
        when(memberRepository.findById("M1")).thenReturn(Optional.of(new Member("M1", "Alice")));
        when(bookRepository.findById("B1")).thenReturn(Optional.of(new Book("B1", "Dune")));
        when(loanRepository.findActiveLoanForBook("B1")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.reserve("M1", "B1", now))
                .isInstanceOf(BookAvailableException.class);
        verify(reservationRepository, never()).save(any());
    }

    @Test
    void reserve_throwsWhenMemberAlreadyReservedSameBook() {
        when(memberRepository.findById("M1")).thenReturn(Optional.of(new Member("M1", "Alice")));
        when(bookRepository.findById("B1")).thenReturn(Optional.of(new Book("B1", "Dune")));
        Loan onLoan = new Loan("M9", "B1", LocalDate.now(), LocalDate.now().plusDays(21));
        when(loanRepository.findActiveLoanForBook("B1")).thenReturn(Optional.of(onLoan));
        when(reservationRepository.findByBookInOrder("B1"))
                .thenReturn(List.of(new Reservation("B1", "M1", now)));

        assertThatThrownBy(() -> service.reserve("M1", "B1", now))
                .isInstanceOf(DuplicateReservationException.class);
        verify(reservationRepository, never()).save(any());
    }

    @Test
    void nextReservation_returnsFirstInQueue() {
        when(reservationRepository.findByBookInOrder("B1")).thenReturn(List.of(
                new Reservation("B1", "Alice", now),
                new Reservation("B1", "Carol", now.plusMinutes(5))));

        Optional<Reservation> next = service.nextReservation("B1");

        assertThat(next).isPresent();
        assertThat(next.get().getMemberId()).isEqualTo("Alice");
    }

    @Test
    void nextReservation_isEmptyWhenNoQueue() {
        when(reservationRepository.findByBookInOrder("B1")).thenReturn(List.of());

        assertThat(service.nextReservation("B1")).isEmpty();
    }
}
