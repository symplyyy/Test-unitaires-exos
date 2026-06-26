package com.ynov.bank.exception;

// -> 400 (montant <= 0)
public class InvalidAmountException extends RuntimeException {

    public InvalidAmountException(String message) {
        super(message);
    }
}
