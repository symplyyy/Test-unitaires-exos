package com.ynov.booking.service;

import com.ynov.booking.dto.CreateReservationRequest;
import com.ynov.booking.exception.AlreadyCancelledException;
import com.ynov.booking.exception.OverlappingReservationException;
import com.ynov.booking.exception.ReservationNotFoundException;
import com.ynov.booking.exception.RoomNotFoundException;
import com.ynov.booking.exception.ValidationException;
import com.ynov.booking.model.Reservation;
import com.ynov.booking.model.ReservationStatus;
import com.ynov.booking.repository.ReservationRepository;
import com.ynov.booking.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final RoomRepository roomRepository;

    public ReservationService(ReservationRepository reservationRepository, RoomRepository roomRepository) {
        this.reservationRepository = reservationRepository;
        this.roomRepository = roomRepository;
    }

    public Reservation create(CreateReservationRequest request) {
        // La salle doit exister
        roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new RoomNotFoundException(request.getRoomId()));

        // Le nom de la personne qui reserve est obligatoire
        if (request.getPersonName() == null || request.getPersonName().isBlank()) {
            throw new ValidationException("Le nom de la personne est obligatoire");
        }

        // Les dates doivent etre presentes
        if (request.getStart() == null || request.getEnd() == null) {
            throw new ValidationException("Les dates de debut et de fin sont obligatoires");
        }

        // La fin doit etre strictement apres le debut
        if (!request.getEnd().isAfter(request.getStart())) {
            throw new ValidationException("La date de fin doit etre apres la date de debut");
        }

        // Deux reservations confirmees ne peuvent pas se chevaucher pour une meme salle
        if (overlapsConfirmedReservation(request.getRoomId(), request.getStart(), request.getEnd())) {
            throw new OverlappingReservationException();
        }

        Reservation reservation = new Reservation(request.getRoomId(), request.getPersonName(),
                request.getStart(), request.getEnd(), ReservationStatus.CONFIRMED);
        return reservationRepository.save(reservation);
    }

    public Reservation getById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new ReservationNotFoundException(id));
    }

    public Reservation cancel(Long id) {
        Reservation reservation = getById(id);

        // Une reservation deja annulee ne peut pas l'etre une seconde fois
        if (reservation.getStatus() == ReservationStatus.CANCELLED) {
            throw new AlreadyCancelledException(id);
        }

        reservation.setStatus(ReservationStatus.CANCELLED);
        return reservationRepository.save(reservation);
    }

    /**
     * Verifie si le creneau [start, end) chevauche une reservation deja confirmee
     * de la meme salle. Les creneaux adjacents (fin == debut) ne se chevauchent pas
     * et les reservations annulees sont ignorees.
     */
    private boolean overlapsConfirmedReservation(Long roomId, LocalDateTime start, LocalDateTime end) {
        List<Reservation> existing = reservationRepository.findByRoomId(roomId);
        for (Reservation r : existing) {
            if (r.getStatus() != ReservationStatus.CONFIRMED) {
                continue;
            }
            boolean overlap = start.isBefore(r.getEnd()) && end.isAfter(r.getStart());
            if (overlap) {
                return true;
            }
        }
        return false;
    }
}
