package com.ynov.bank.exception;

// -> 409 (numero deja pris)
public class AccountAlreadyExistsException extends RuntimeException {

    public AccountAlreadyExistsException(String number) {
        super("Un compte existe deja avec le numero : " + number);
    }
}
