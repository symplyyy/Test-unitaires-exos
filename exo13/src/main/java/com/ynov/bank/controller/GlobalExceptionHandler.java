package com.ynov.bank.controller;

import com.ynov.bank.exception.AccountAlreadyExistsException;
import com.ynov.bank.exception.AccountNotFoundException;
import com.ynov.bank.exception.InsufficientFundsException;
import com.ynov.bank.exception.InvalidAmountException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

// On attrape les exceptions metier ici pour renvoyer le bon code HTTP
// au lieu d'un 500 par defaut.
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidAmountException.class)
    public ResponseEntity<Map<String, String>> handleInvalidAmount(InvalidAmountException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", ex.getMessage()));
    }

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleNotFound(AccountNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", ex.getMessage()));
    }

    // numero deja pris ou solde insuffisant -> 409
    @ExceptionHandler({AccountAlreadyExistsException.class, InsufficientFundsException.class})
    public ResponseEntity<Map<String, String>> handleConflict(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", ex.getMessage()));
    }
}
