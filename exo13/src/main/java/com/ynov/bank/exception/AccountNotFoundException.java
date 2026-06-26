package com.ynov.bank.exception;

// -> 404
public class AccountNotFoundException extends RuntimeException {

    public AccountNotFoundException(String number) {
        super("Compte introuvable : " + number);
    }
}
