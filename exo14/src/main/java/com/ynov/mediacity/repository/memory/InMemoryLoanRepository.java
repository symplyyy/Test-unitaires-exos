package com.ynov.mediacity.repository.memory;

import com.ynov.mediacity.model.Loan;
import com.ynov.mediacity.repository.LoanRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryLoanRepository implements LoanRepository {

    private final List<Loan> loans = new ArrayList<>();
    private final AtomicLong sequence = new AtomicLong(0);

    @Override
    public Loan save(Loan loan) {
        if (loan.getId() == null) {
            loan.setId("L" + sequence.incrementAndGet());
            loans.add(loan);
        }
        return loan;
    }

    @Override
    public Optional<Loan> findById(String id) {
        return loans.stream().filter(l -> l.getId().equals(id)).findFirst();
    }

    @Override
    public Optional<Loan> findActiveLoanForBook(String bookId) {
        return loans.stream()
                .filter(l -> l.getBookId().equals(bookId) && !l.isReturned())
                .findFirst();
    }
}
