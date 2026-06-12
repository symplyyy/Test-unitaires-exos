package com.ynov.booking.controller;

import com.ynov.booking.exception.AlreadyCancelledException;
import com.ynov.booking.exception.OverlappingReservationException;
import com.ynov.booking.exception.ReservationNotFoundException;
import com.ynov.booking.exception.RoomNotFoundException;
import com.ynov.booking.exception.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Map<String, String>> handleValidation(ValidationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", ex.getMessage()));
    }

    @ExceptionHandler({RoomNotFoundException.class, ReservationNotFoundException.class})
    public ResponseEntity<Map<String, String>> handleNotFound(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", ex.getMessage()));
    }

    @ExceptionHandler({OverlappingReservationException.class, AlreadyCancelledException.class})
    public ResponseEntity<Map<String, String>> handleConflict(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", ex.getMessage()));
    }
}
