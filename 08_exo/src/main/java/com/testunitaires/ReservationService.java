package com.testunitaires;

import java.util.Optional;

public class ReservationService {

    private final SalleRepository salleRepository;
    private final ReservationRepository reservationRepository;
    private final NotificationService notificationService;

    public ReservationService(SalleRepository salleRepository,
                              ReservationRepository reservationRepository,
                              NotificationService notificationService) {
        this.salleRepository = salleRepository;
        this.reservationRepository = reservationRepository;
        this.notificationService = notificationService;
    }

    public ConfirmationReservation reserver(Reservation reservation) {
        Salle salle = salleRepository.findByCode(reservation.getCodeSalle())
                .orElseThrow(() -> new ReservationRefuseeException("salle inconnue"));

        if (reservation.getNombreParticipants() > salle.getCapaciteMax()) {
            throw new ReservationRefuseeException("capacité insuffisante");
        }

        if (!reservation.getDateFin().isAfter(reservation.getDateDebut())) {
            throw new ReservationRefuseeException("période invalide");
        }

        boolean conflit = reservationRepository.findByCodeSalle(reservation.getCodeSalle())
                .stream()
                .anyMatch(existante -> chevauche(existante, reservation));
        if (conflit) {
            throw new ReservationRefuseeException("salle déjà réservée");
        }

        ConfirmationReservation confirmation = new ConfirmationReservation(
                reservation.getEmailUtilisateur(),
                salle.getCode(),
                salle.getNom(),
                reservation.getNombreParticipants(),
                reservation.getDateDebut(),
                reservation.getDateFin(),
                "Réservation confirmée");

        notificationService.envoyerConfirmation(reservation.getEmailUtilisateur(), confirmation);

        return confirmation;
    }

    private boolean chevauche(Reservation a, Reservation b) {
        return a.getDateDebut().isBefore(b.getDateFin())
                && b.getDateDebut().isBefore(a.getDateFin());
    }
}
