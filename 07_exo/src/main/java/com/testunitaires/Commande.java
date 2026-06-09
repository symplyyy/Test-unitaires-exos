package com.testunitaires;

public class Commande {

    private final String emailClient;
    private final String referenceProduit;
    private final int quantite;

    public Commande(String emailClient, String referenceProduit, int quantite) {
        this.emailClient = emailClient;
        this.referenceProduit = referenceProduit;
        this.quantite = quantite;
    }

    public String getEmailClient() {
        return emailClient;
    }

    public String getReferenceProduit() {
        return referenceProduit;
    }

    public int getQuantite() {
        return quantite;
    }
}
