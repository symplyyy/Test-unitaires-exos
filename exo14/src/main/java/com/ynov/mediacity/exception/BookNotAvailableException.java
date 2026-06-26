package com.ynov.mediacity.exception;

// L'ouvrage est deja emprunte, on ne peut pas le preter une 2e fois.
public class BookNotAvailableException extends RuntimeException {

    public BookNotAvailableException(String bookId) {
        super("Ouvrage deja emprunte : " + bookId);
    }
}
