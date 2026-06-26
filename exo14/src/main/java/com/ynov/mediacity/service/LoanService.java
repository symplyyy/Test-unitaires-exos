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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

// Coeur metier des prets : emprunt, retour, penalites et suspension.
public class LoanService {

    // duree d'un pret
    private static final int LOAN_DURATION_DAYS = 21;
    // penalite par jour de retard
    private static final BigDecimal PENALTY_PER_DAY = new BigDecimal("0.15");
    // au dela de ce nombre de jours, le retard est considere comme "important"
    private static final int IMPORTANT_LATE_THRESHOLD_DAYS = 7;
    // nombre de retards importants avant suspension
    private static final int MAX_IMPORTANT_LATE_BEFORE_SUSPENSION = 3;

    private final MemberRepository memberRepository;
    private final BookRepository bookRepository;
    private final LoanRepository loanRepository;

    public LoanService(MemberRepository memberRepository, BookRepository bookRepository, LoanRepository loanRepository) {
        this.memberRepository = memberRepository;
        this.bookRepository = bookRepository;
        this.loanRepository = loanRepository;
    }

    public Loan lend(String memberId, String bookId, LocalDate today) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException(memberId));
        if (member.isSuspended()) {
            throw new MemberSuspendedException(memberId);
        }

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException(bookId));

        // un ouvrage deja emprunte n'est plus disponible
        if (loanRepository.findActiveLoanForBook(book.getId()).isPresent()) {
            throw new BookNotAvailableException(bookId);
        }

        Loan loan = new Loan(memberId, bookId, today, today.plusDays(LOAN_DURATION_DAYS));
        return loanRepository.save(loan);
    }

    // penalite = 0,15 € par jour de retard (0 si rendu a temps)
    public BigDecimal penaltyFor(Loan loan, LocalDate returnDate) {
        long daysLate = ChronoUnit.DAYS.between(loan.getDueDate(), returnDate);
        if (daysLate <= 0) {
            return BigDecimal.ZERO;
        }
        return PENALTY_PER_DAY.multiply(BigDecimal.valueOf(daysLate));
    }

    public Loan returnBook(String loanId, LocalDate returnDate) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new LoanNotFoundException(loanId));

        loan.markReturned(returnDate);
        loan.setPenalty(penaltyFor(loan, returnDate));

        long daysLate = ChronoUnit.DAYS.between(loan.getDueDate(), returnDate);
        if (daysLate > IMPORTANT_LATE_THRESHOLD_DAYS) {
            registerImportantLate(loan.getMemberId());
        }

        return loanRepository.save(loan);
    }

    // ajoute un retard important a l'adherent et le suspend si le seuil est atteint
    private void registerImportantLate(String memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException(memberId));
        member.addImportantLate();
        if (member.getImportantLateCount() >= MAX_IMPORTANT_LATE_BEFORE_SUSPENSION) {
            member.suspend();
        }
        memberRepository.save(member);
    }
}
