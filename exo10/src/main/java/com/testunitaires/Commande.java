package com.testunitaires;

import java.util.ArrayList;
import java.util.List;

public class Commande {
  private final String id;
  private final List<LigneCommande> lignes = new ArrayList<>();
  private boolean validee;

  public Commande(String id) {
    this.id = id;
  }

  public void ajouterProduit(Produit produit) {
    LigneCommande ligne = trouverLigne(produit.getId());
    if (ligne != null) {
      ligne.augmenter();
    } else {
      lignes.add(new LigneCommande(produit, 1));
    }
  }

  public void retirerProduit(String produitId) {
    LigneCommande ligne = trouverLigne(produitId);
    if (ligne == null) {
      throw new ProduitIntrouvableException("produit absent de la commande");
    }
    if (ligne.getQuantite() > 1) {
      ligne.diminuer();
    } else {
      lignes.remove(ligne);
    }
  }

  public int quantite(String produitId) {
    LigneCommande ligne = trouverLigne(produitId);
    return ligne == null ? 0 : ligne.getQuantite();
  }

  public void valider() {
    validee = true;
  }

  private LigneCommande trouverLigne(String produitId) {
    return lignes.stream()
        .filter(ligne -> ligne.getProduit().getId().equals(produitId))
        .findFirst()
        .orElse(null);
  }

  public String getId() {
    return id;
  }

  public List<LigneCommande> getLignes() {
    return lignes;
  }

  public boolean estValidee() {
    return validee;
  }
}
