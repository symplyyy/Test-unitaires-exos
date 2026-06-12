package com.ynov.booking.model;

import java.time.LocalDateTime;

public class Reservation {

    private Long id;
    private Long roomId;
    private String personName;
    private LocalDateTime start;
    private LocalDateTime end;
    private ReservationStatus status;

    public Reservation() {
    }

    public Reservation(Long roomId, String personName, LocalDateTime start, LocalDateTime end, ReservationStatus status) {
        this.roomId = roomId;
        this.personName = personName;
        this.start = start;
        this.end = end;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }
}
