package com.testunitaires;

import java.time.LocalDateTime;

public class Reservation {

    private final String emailUtilisateur;
    private final String codeSalle;
    private final int nombreParticipants;
    private final LocalDateTime dateDebut;
    private final LocalDateTime dateFin;

    public Reservation(String emailUtilisateur, String codeSalle, int nombreParticipants,
                       LocalDateTime dateDebut, LocalDateTime dateFin) {
        this.emailUtilisateur = emailUtilisateur;
        this.codeSalle = codeSalle;
        this.nombreParticipants = nombreParticipants;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
    }

    public String getEmailUtilisateur() {
        return emailUtilisateur;
    }

    public String getCodeSalle() {
        return codeSalle;
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
}
