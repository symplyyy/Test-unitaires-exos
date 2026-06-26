package com.ynov.mediacity.exception;

public class BookNotFoundException extends RuntimeException {

    public BookNotFoundException(String id) {
        super("Ouvrage introuvable : " + id);
    }
}
