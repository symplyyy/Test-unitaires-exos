package com.ynov.mediacity.model;

import java.math.BigDecimal;
import java.time.LocalDate;

// Un pret : qui a emprunte quoi, quand, et quand il faut rendre.
public class Loan {

    private String id;
    private final String memberId;
    private final String bookId;
    private final LocalDate loanDate;
    private final LocalDate dueDate;
    private LocalDate returnDate;
    private boolean returned;
    private BigDecimal penalty = BigDecimal.ZERO;

    public Loan(String memberId, String bookId, LocalDate loanDate, LocalDate dueDate) {
        this.memberId = memberId;
        this.bookId = bookId;
        this.loanDate = loanDate;
        this.dueDate = dueDate;
    }

    public void markReturned(LocalDate date) {
        this.returnDate = date;
        this.returned = true;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMemberId() {
        return memberId;
    }

    public String getBookId() {
        return bookId;
    }

    public LocalDate getLoanDate() {
        return loanDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public boolean isReturned() {
        return returned;
    }

    public BigDecimal getPenalty() {
        return penalty;
    }

    public void setPenalty(BigDecimal penalty) {
        this.penalty = penalty;
    }
}
