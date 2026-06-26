package com.ynov.mediacity.service;

import com.ynov.mediacity.exception.BookNotAvailableException;
import com.ynov.mediacity.exception.BookNotFoundException;
import com.ynov.mediacity.exception.LoanNotFoundException;
import com.ynov.mediacity.exception.MemberNotFoundException;
import com.ynov.mediacity.exception.MemberSuspendedException;
import com.ynov.mediacity.model.Book;
import com.ynov.mediacity.model.Loan;
import com.ynov.mediacity.model.Member;
import com.ynov.mediacity.repository.BookRepository;
import com.ynov.mediacity.repository.LoanRepository;
import com.ynov.mediacity.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoanServiceTest {

    @Mock
    private MemberRepository memberRepository;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private LoanRepository loanRepository;

    @InjectMocks
    private LoanService service;

    private final LocalDate today = LocalDate.of(2026, 1, 1);

    // --- emprunt ---

    @Test
    void lend_createsLoanDueIn21Days() {
        when(memberRepository.findById("M1")).thenReturn(Optional.of(new Member("M1", "Alice")));
        when(bookRepository.findById("B1")).thenReturn(Optional.of(new Book("B1", "Dune")));
        when(loanRepository.findActiveLoanForBook("B1")).thenReturn(Optional.empty());
        when(loanRepository.save(any(Loan.class))).thenAnswer(inv -> inv.getArgument(0));

        Loan loan = service.lend("M1", "B1", today);

        assertThat(loan.getLoanDate()).isEqualTo(today);
        assertThat(loan.getDueDate()).isEqualTo(LocalDate.of(2026, 1, 22)); // +21 jours
        assertThat(loan.isReturned()).isFalse();
    }

    @Test
    void lend_throwsWhenMemberNotFound() {
        when(memberRepository.findById("M1")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.lend("M1", "B1", today))
                .isInstanceOf(MemberNotFoundException.class);
        verify(loanRepository, never()).save(any());
    }

    @Test
    void lend_throwsWhenMemberIsSuspended() {
        Member suspended = new Member("M1", "Alice");
        suspended.suspend();
        when(memberRepository.findById("M1")).thenReturn(Optional.of(suspended));

        assertThatThrownBy(() -> service.lend("M1", "B1", today))
                .isInstanceOf(MemberSuspendedException.class);
        verify(loanRepository, never()).save(any());
    }

    @Test
    void lend_throwsWhenBookNotFound() {
        when(memberRepository.findById("M1")).thenReturn(Optional.of(new Member("M1", "Alice")));
        when(bookRepository.findById("B1")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.lend("M1", "B1", today))
                .isInstanceOf(BookNotFoundException.class);
        verify(loanRepository, never()).save(any());
    }

    @Test
    void lend_throwsWhenBookAlreadyOnLoan() {
        when(memberRepository.findById("M1")).thenReturn(Optional.of(new Member("M1", "Alice")));
        when(bookRepository.findById("B1")).thenReturn(Optional.of(new Book("B1", "Dune")));
        Loan existing = new Loan("M9", "B1", today, today.plusDays(21));
        when(loanRepository.findActiveLoanForBook("B1")).thenReturn(Optional.of(existing));

        assertThatThrownBy(() -> service.lend("M1", "B1", today))
                .isInstanceOf(BookNotAvailableException.class);
        verify(loanRepository, never()).save(any());
    }

    // --- penalites ---

    @Test
    void penalty_isZeroWhenReturnedOnTime() {
        Loan loan = new Loan("M1", "B1", today, today.plusDays(21));

        assertThat(service.penaltyFor(loan, loan.getDueDate())).isEqualByComparingTo("0");
    }

    @Test
    void penalty_isZeroWhenReturnedEarly() {
        Loan loan = new Loan("M1", "B1", today, today.plusDays(21));

        assertThat(service.penaltyFor(loan, loan.getDueDate().minusDays(3))).isEqualByComparingTo("0");
    }

    @Test
    void penalty_isFifteenCentsPerDayLate() {
        Loan loan = new Loan("M1", "B1", today, today.plusDays(21));

        // 10 jours de retard -> 10 * 0,15 = 1,50
        assertThat(service.penaltyFor(loan, loan.getDueDate().plusDays(10))).isEqualByComparingTo("1.50");
    }

    // --- retour ---

    @Test
    void returnBook_marksReturnedAndComputesPenalty() {
        Loan loan = new Loan("M1", "B1", today, today.plusDays(21));
        loan.setId("L1");
        when(loanRepository.findById("L1")).thenReturn(Optional.of(loan));
        when(loanRepository.save(any(Loan.class))).thenAnswer(inv -> inv.getArgument(0));

        // 3 jours de retard : penalite mais retard non "important"
        Loan result = service.returnBook("L1", loan.getDueDate().plusDays(3));

        assertThat(result.isReturned()).isTrue();
        assertThat(result.getPenalty()).isEqualByComparingTo("0.45");
        verify(memberRepository, never()).save(any());
    }

    @Test
    void returnBook_throwsWhenLoanNotFound() {
        when(loanRepository.findById("L404")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.returnBook("L404", today))
                .isInstanceOf(LoanNotFoundException.class);
    }

    @Test
    void returnBook_countsImportantLateWhenVeryLate() {
        Loan loan = new Loan("M1", "B1", today, today.plusDays(21));
        loan.setId("L1");
        Member member = new Member("M1", "Alice");
        when(loanRepository.findById("L1")).thenReturn(Optional.of(loan));
        when(memberRepository.findById("M1")).thenReturn(Optional.of(member));
        when(loanRepository.save(any(Loan.class))).thenAnswer(inv -> inv.getArgument(0));

        // 10 jours de retard (> 7) -> retard important
        service.returnBook("L1", loan.getDueDate().plusDays(10));

        assertThat(member.getImportantLateCount()).isEqualTo(1);
        assertThat(member.isSuspended()).isFalse();
        verify(memberRepository).save(member);
    }

    @Test
    void returnBook_suspendsMemberAfterThreeImportantLates() {
        Loan loan = new Loan("M1", "B1", today, today.plusDays(21));
        loan.setId("L1");
        Member member = new Member("M1", "Alice");
        member.addImportantLate();
        member.addImportantLate(); // deja 2 retards importants
        when(loanRepository.findById("L1")).thenReturn(Optional.of(loan));
        when(memberRepository.findById("M1")).thenReturn(Optional.of(member));
        when(loanRepository.save(any(Loan.class))).thenAnswer(inv -> inv.getArgument(0));

        // le 3e retard important declenche la suspension
        service.returnBook("L1", loan.getDueDate().plusDays(15));

        assertThat(member.getImportantLateCount()).isEqualTo(3);
        assertThat(member.isSuspended()).isTrue();
    }
}
