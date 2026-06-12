package com.ynov.booking.controller;

import com.ynov.booking.dto.CreateReservationRequest;
import com.ynov.booking.model.Reservation;
import com.ynov.booking.service.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService service;

    public ReservationController(ReservationService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Reservation> create(@RequestBody CreateReservationRequest request) {
        Reservation created = service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public Reservation getOne(@PathVariable Long id) {
        return service.getById(id);
    }

    @PatchMapping("/{id}/cancel")
    public Reservation cancel(@PathVariable Long id) {
        return service.cancel(id);
    }
}
