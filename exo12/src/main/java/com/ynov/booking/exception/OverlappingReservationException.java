package com.ynov.booking.exception;

public class OverlappingReservationException extends RuntimeException {

    public OverlappingReservationException() {
        super("Le creneau chevauche une reservation existante");
    }
}
