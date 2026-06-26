package com.ynov.mediacity.service;

import com.ynov.mediacity.exception.BookAvailableException;
import com.ynov.mediacity.exception.BookNotFoundException;
import com.ynov.mediacity.exception.DuplicateReservationException;
import com.ynov.mediacity.exception.MemberNotFoundException;
import com.ynov.mediacity.exception.MemberSuspendedException;
import com.ynov.mediacity.model.Member;
import com.ynov.mediacity.model.Reservation;
import com.ynov.mediacity.repository.BookRepository;
import com.ynov.mediacity.repository.LoanRepository;
import com.ynov.mediacity.repository.MemberRepository;
import com.ynov.mediacity.repository.ReservationRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

// Gestion des reservations sur les ouvrages indisponibles.
public class ReservationService {

    private final MemberRepository memberRepository;
    private final BookRepository bookRepository;
    private final LoanRepository loanRepository;
    private final ReservationRepository reservationRepository;

    public ReservationService(MemberRepository memberRepository, BookRepository bookRepository,
                              LoanRepository loanRepository, ReservationRepository reservationRepository) {
        this.memberRepository = memberRepository;
        this.bookRepository = bookRepository;
        this.loanRepository = loanRepository;
        this.reservationRepository = reservationRepository;
    }

    public Reservation reserve(String memberId, String bookId, LocalDateTime now) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException(memberId));
        if (member.isSuspended()) {
            throw new MemberSuspendedException(memberId);
        }

        bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException(bookId));

        // on ne reserve que ce qui est indisponible (donc actuellement emprunte)
        if (loanRepository.findActiveLoanForBook(bookId).isEmpty()) {
            throw new BookAvailableException(bookId);
        }

        boolean alreadyReserved = reservationRepository.findByBookInOrder(bookId).stream()
                .anyMatch(r -> r.getMemberId().equals(memberId));
        if (alreadyReserved) {
            throw new DuplicateReservationException(memberId, bookId);
        }

        return reservationRepository.save(new Reservation(bookId, memberId, now));
    }

    // le premier de la file : prochain a pouvoir emprunter quand l'ouvrage revient
    public Optional<Reservation> nextReservation(String bookId) {
        List<Reservation> queue = reservationRepository.findByBookInOrder(bookId);
        return queue.isEmpty() ? Optional.empty() : Optional.of(queue.get(0));
    }
}
