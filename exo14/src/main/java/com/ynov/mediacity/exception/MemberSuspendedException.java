package com.ynov.mediacity.exception;

// Adherent suspendu : il ne peut plus emprunter ni reserver.
public class MemberSuspendedException extends RuntimeException {

    public MemberSuspendedException(String id) {
        super("Adherent suspendu : " + id);
    }
}
