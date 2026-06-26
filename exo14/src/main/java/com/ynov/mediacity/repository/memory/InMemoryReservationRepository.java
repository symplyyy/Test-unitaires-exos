package com.ynov.mediacity.repository.memory;

import com.ynov.mediacity.model.Reservation;
import com.ynov.mediacity.repository.ReservationRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryReservationRepository implements ReservationRepository {

    // une simple liste : l'ordre d'insertion = la file d'attente
    private final List<Reservation> reservations = new ArrayList<>();
    private final AtomicLong sequence = new AtomicLong(0);

    @Override
    public Reservation save(Reservation reservation) {
        if (reservation.getId() == null) {
            reservation.setId("R" + sequence.incrementAndGet());
        }
        reservations.add(reservation);
        return reservation;
    }

    @Override
    public List<Reservation> findByBookInOrder(String bookId) {
        List<Reservation> result = new ArrayList<>();
        for (Reservation r : reservations) {
            if (r.getBookId().equals(bookId)) {
                result.add(r);
            }
        }
        return result;
    }
}
