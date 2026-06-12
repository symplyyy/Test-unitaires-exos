package com.ynov.booking.exception;

public class RoomNotFoundException extends RuntimeException {

    public RoomNotFoundException(Long id) {
        super("Salle introuvable : " + id);
    }
}
