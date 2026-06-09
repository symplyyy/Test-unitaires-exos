package com.testunitaires;

public class Recu {

    private final String emailClient;
    private final String referenceProduit;
    private final String nomProduit;
    private final int quantite;
    private final double montantTotal;
    private final String message;

    public Recu(String emailClient, String referenceProduit, String nomProduit,
                int quantite, double montantTotal, String message) {
        this.emailClient = emailClient;
        this.referenceProduit = referenceProduit;
        this.nomProduit = nomProduit;
        this.quantite = quantite;
        this.montantTotal = montantTotal;
        this.message = message;
    }

    public String getEmailClient() {
        return emailClient;
    }

    public String getReferenceProduit() {
        return referenceProduit;
    }

    public String getNomProduit() {
        return nomProduit;
    }

    public int getQuantite() {
        return quantite;
    }

    public double getMontantTotal() {
        return montantTotal;
    }

    public String getMessage() {
        return message;
    }
}
