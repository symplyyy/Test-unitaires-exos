package com.ynov.mediacity.exception;

// On ne reserve que les ouvrages indisponibles : si dispo, autant l'emprunter.
public class BookAvailableException extends RuntimeException {

    public BookAvailableException(String bookId) {
        super("Ouvrage disponible, reservation inutile : " + bookId);
    }
}
