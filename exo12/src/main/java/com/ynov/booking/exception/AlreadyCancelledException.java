package com.ynov.booking.exception;

public class AlreadyCancelledException extends RuntimeException {

    public AlreadyCancelledException(Long id) {
        super("La reservation est deja annulee : " + id);
    }
}
