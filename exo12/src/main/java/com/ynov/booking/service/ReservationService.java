package com.ynov.booking.service;

import com.ynov.booking.dto.CreateReservationRequest;
import com.ynov.booking.model.Reservation;
import com.ynov.booking.repository.ReservationRepository;
import com.ynov.booking.repository.RoomRepository;
import org.springframework.stereotype.Service;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final RoomRepository roomRepository;

    public ReservationService(ReservationRepository reservationRepository, RoomRepository roomRepository) {
        this.reservationRepository = reservationRepository;
        this.roomRepository = roomRepository;
    }

    public Reservation create(CreateReservationRequest request) {
        throw new UnsupportedOperationException("pas encore implémenté");
    }

    public Reservation getById(Long id) {
        throw new UnsupportedOperationException("pas encore implémenté");
    }

    public Reservation cancel(Long id) {
        throw new UnsupportedOperationException("pas encore implémenté");
    }
}
