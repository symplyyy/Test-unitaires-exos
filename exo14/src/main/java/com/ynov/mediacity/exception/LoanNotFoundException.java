package com.ynov.mediacity.exception;

public class LoanNotFoundException extends RuntimeException {

    public LoanNotFoundException(String id) {
        super("Pret introuvable : " + id);
    }
}
