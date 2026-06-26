package com.ynov.mediacity.repository;

import com.ynov.mediacity.model.Loan;

import java.util.Optional;

public interface LoanRepository {

    Loan save(Loan loan);

    Optional<Loan> findById(String id);

    // le pret en cours (non rendu) pour un ouvrage, s'il existe
    Optional<Loan> findActiveLoanForBook(String bookId);
}
