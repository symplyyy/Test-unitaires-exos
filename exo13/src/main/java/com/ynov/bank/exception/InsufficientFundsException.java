package com.ynov.bank.exception;

// -> 409 (pas assez d'argent)
public class InsufficientFundsException extends RuntimeException {

    public InsufficientFundsException(String number) {
        super("Fonds insuffisants sur le compte : " + number);
    }
}
