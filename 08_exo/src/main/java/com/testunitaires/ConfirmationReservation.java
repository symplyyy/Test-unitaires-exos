package com.testunitaires;

import java.time.LocalDateTime;

public class ConfirmationReservation {

    private final String emailUtilisateur;
    private final String codeSalle;
    private final String nomSalle;
    private final int nombreParticipants;
    private final LocalDateTime dateDebut;
    private final LocalDateTime dateFin;
    private final String message;

    public ConfirmationReservation(String emailUtilisateur, String codeSalle, String nomSalle,
                                   int nombreParticipants, LocalDateTime dateDebut, LocalDateTime dateFin,
                                   String message) {
        this.emailUtilisateur = emailUtilisateur;
        this.codeSalle = codeSalle;
        this.nomSalle = nomSalle;
        this.nombreParticipants = nombreParticipants;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.message = message;
    }

    public String getEmailUtilisateur() {
        return emailUtilisateur;
    }

    public String getCodeSalle() {
        return codeSalle;
    }

    public String getNomSalle() {
        return nomSalle;
    }

    public int getNombreParticipants() {
        return nombreParticipants;
    }

    public LocalDateTime getDateDebut() {
        return dateDebut;
    }

    public LocalDateTime getDateFin() {
        return dateFin;
    }

    public String getMessage() {
        return message;
    }
}
