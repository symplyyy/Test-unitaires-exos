package com.ynov.mediacity.exception;

// Un adherent ne peut pas reserver deux fois le meme ouvrage.
public class DuplicateReservationException extends RuntimeException {

    public DuplicateReservationException(String memberId, String bookId) {
        super("Reservation deja existante pour l'adherent " + memberId + " sur l'ouvrage " + bookId);
    }
}
