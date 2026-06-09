package com.testunitaires;

public class Produit {

    private final String reference;
    private final String nom;
    private final double prixUnitaire;
    private final int stockDisponible;

    public Produit(String reference, String nom, double prixUnitaire, int stockDisponible) {
        this.reference = reference;
        this.nom = nom;
        this.prixUnitaire = prixUnitaire;
        this.stockDisponible = stockDisponible;
    }

    public String getReference() {
        return reference;
    }

    public String getNom() {
        return nom;
    }

    public double getPrixUnitaire() {
        return prixUnitaire;
    }

    public int getStockDisponible() {
        return stockDisponible;
    }
}
