package com.ynov.booking.service;

import com.ynov.booking.dto.CreateReservationRequest;
import com.ynov.booking.exception.RoomNotFoundException;
import com.ynov.booking.model.Reservation;
import com.ynov.booking.model.ReservationStatus;
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
        roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new RoomNotFoundException(request.getRoomId()));

        // TODO valider le nom, le creneau et verifier les chevauchements
        Reservation reservation = new Reservation(request.getRoomId(), request.getPersonName(),
                request.getStart(), request.getEnd(), ReservationStatus.CONFIRMED);
        return reservationRepository.save(reservation);
    }

    public Reservation getById(Long id) {
        // TODO a faire
        return null;
    }

    public Reservation cancel(Long id) {
        // TODO a faire
        return null;
    }
}
