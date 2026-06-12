package com.testunitaires;

public class Produit {
  private final String id;
  private final String nom;
  private final String categorie;
  private final double prix;

  public Produit(String id, String nom, String categorie, double prix) {
    this.id = id;
    this.nom = nom;
    this.categorie = categorie;
    this.prix = prix;
  }

  public String getId() {
    return id;
  }

  public String getNom() {
    return nom;
  }

  public String getCategorie() {
    return categorie;
  }

  public double getPrix() {
    return prix;
  }
}
