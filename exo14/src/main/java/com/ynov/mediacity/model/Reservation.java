package com.ynov.mediacity.model;

import java.time.LocalDateTime;

// Une reservation sur un ouvrage indisponible. L'ordre d'arrivee fait la file d'attente.
public class Reservation {

    private String id;
    private final String bookId;
    private final String memberId;
    private final LocalDateTime createdAt;

    public Reservation(String bookId, String memberId, LocalDateTime createdAt) {
        this.bookId = bookId;
        this.memberId = memberId;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBookId() {
        return bookId;
    }

    public String getMemberId() {
        return memberId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
