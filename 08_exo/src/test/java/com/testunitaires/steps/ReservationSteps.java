package com.testunitaires.steps;

import com.testunitaires.ConfirmationReservation;
import com.testunitaires.NotificationService;
import com.testunitaires.Reservation;
import com.testunitaires.ReservationRefuseeException;
import com.testunitaires.ReservationRepository;
import com.testunitaires.ReservationService;
import com.testunitaires.Salle;
import com.testunitaires.SalleRepository;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ReservationSteps {

    private final SalleRepository salleRepository = mock(SalleRepository.class);
    private final ReservationRepository reservationRepository = mock(ReservationRepository.class);
    private final NotificationService notificationService = mock(NotificationService.class);
    private final ReservationService service =
            new ReservationService(salleRepository, reservationRepository, notificationService);

    private ConfirmationReservation confirmation;
    private ReservationRefuseeException refus;

    @Given("la salle {string} nommée {string} d'une capacité de {int}")
    public void la_salle_existe(String code, String nom, int capacite) {
        Salle salle = new Salle(code, nom, capacite);
        when(salleRepository.findByCode(code)).thenReturn(Optional.of(salle));
    }

    @Given("une réservation existante sur la salle {string} du {string} au {string}")
    public void une_reservation_existante(String code, String debut, String fin) {
        Reservation existante = new Reservation(
                "occupant@boite.com", code, 1,
                LocalDateTime.parse(debut), LocalDateTime.parse(fin));
        when(reservationRepository.findByCodeSalle(code)).thenReturn(List.of(existante));
    }

    @When("{string} réserve la salle {string} pour {int} participants du {string} au {string}")
    public void reserve(String email, String code, int participants, String debut, String fin) {
        Reservation reservation = new Reservation(
                email, code, participants,
                LocalDateTime.parse(debut), LocalDateTime.parse(fin));
        try {
            confirmation = service.reserver(reservation);
            refus = null;
        } catch (ReservationRefuseeException e) {
            refus = e;
            confirmation = null;
        }
    }

    @Then("la réservation est acceptée")
    public void la_reservation_est_acceptee() {
        assertNull(refus);
        assertNotNull(confirmation);
    }

    @Then("la réservation est refusée")
    public void la_reservation_est_refusee() {
        assertNull(confirmation);
        assertNotNull(refus);
    }

    @Then("la confirmation porte la salle {string} nommée {string} pour {int} participants")
    public void la_confirmation_porte(String code, String nom, int participants) {
        assertEquals(code, confirmation.getCodeSalle());
        assertEquals(nom, confirmation.getNomSalle());
        assertEquals(participants, confirmation.getNombreParticipants());
    }

    @Then("la confirmation couvre le créneau {string} - {string}")
    public void la_confirmation_couvre(String debut, String fin) {
        assertEquals(LocalDateTime.parse(debut), confirmation.getDateDebut());
        assertEquals(LocalDateTime.parse(fin), confirmation.getDateFin());
    }

    @Then("la confirmation est destinée à {string} avec le message {string}")
    public void la_confirmation_destinee(String email, String message) {
        assertEquals(email, confirmation.getEmailUtilisateur());
        assertEquals(message, confirmation.getMessage());
    }

    @Then("le motif de refus est {string}")
    public void le_motif_de_refus_est(String motif) {
        assertEquals(motif, refus.getMessage());
    }

    @Then("une confirmation est envoyée à {string}")
    public void une_confirmation_est_envoyee(String email) {
        verify(notificationService).envoyerConfirmation(eq(email), any(ConfirmationReservation.class));
    }

    @Then("aucune confirmation n'est envoyée")
    public void aucune_confirmation_envoyee() {
        verify(notificationService, never()).envoyerConfirmation(any(), any());
    }
}
