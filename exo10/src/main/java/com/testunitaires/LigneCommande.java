package com.testunitaires;

public class LigneCommande {
  private final Produit produit;
  private int quantite;

  public LigneCommande(Produit produit, int quantite) {
    this.produit = produit;
    this.quantite = quantite;
  }

  public void augmenter() {
    quantite++;
  }

  public void diminuer() {
    quantite--;
  }

  public Produit getProduit() {
    return produit;
  }

  public int getQuantite() {
    return quantite;
  }
}
