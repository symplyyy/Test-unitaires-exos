package com.ynov.booking.repository;

import com.ynov.booking.model.Reservation;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class ReservationRepository {

    private final Map<Long, Reservation> reservations = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong(0);

    public Reservation save(Reservation reservation) {
        if (reservation.getId() == null) {
            reservation.setId(sequence.incrementAndGet());
        }
        reservations.put(reservation.getId(), reservation);
        return reservation;
    }

    public Optional<Reservation> findById(Long id) {
        return Optional.ofNullable(reservations.get(id));
    }

    public List<Reservation> findAll() {
        return new ArrayList<>(reservations.values());
    }

    public List<Reservation> findByRoomId(Long roomId) {
        List<Reservation> result = new ArrayList<>();
        for (Reservation r : reservations.values()) {
            if (r.getRoomId().equals(roomId)) {
                result.add(r);
            }
        }
        return result;
    }
}
