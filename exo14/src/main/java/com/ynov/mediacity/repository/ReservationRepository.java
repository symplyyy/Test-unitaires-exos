package com.ynov.mediacity.repository;

import com.ynov.mediacity.model.Reservation;

import java.util.List;

public interface ReservationRepository {

    Reservation save(Reservation reservation);

    // toutes les reservations d'un ouvrage, dans l'ordre d'arrivee (file d'attente)
    List<Reservation> findByBookInOrder(String bookId);
}
