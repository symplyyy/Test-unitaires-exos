package com.ynov.tickets.exception;

import com.ynov.tickets.model.Status;

public class InvalidStatusTransitionException extends RuntimeException {

    public InvalidStatusTransitionException(Status from, Status to) {
        super("Transition interdite : " + from + " vers " + to);
    }
}
