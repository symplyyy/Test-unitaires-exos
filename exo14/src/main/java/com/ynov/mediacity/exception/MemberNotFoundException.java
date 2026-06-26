package com.ynov.mediacity.exception;

public class MemberNotFoundException extends RuntimeException {

    public MemberNotFoundException(String id) {
        super("Adherent introuvable : " + id);
    }
}
