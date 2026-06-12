package com.testunitaires;

import java.util.List;

public class CatalogueService {
  private final ProduitRepository produitRepository;

  public CatalogueService(ProduitRepository produitRepository) {
    this.produitRepository = produitRepository;
  }

  public List<Produit> rechercher(String motCle) {
    String terme = motCle.toLowerCase();
    return produitRepository.findAll().stream()
        .filter(produit -> produit.getNom().toLowerCase().contains(terme))
        .toList();
  }

  public List<Produit> rechercherParPrixMax(double prixMax) {
    return produitRepository.findAll().stream()
        .filter(produit -> produit.getPrix() <= prixMax)
        .toList();
  }

  public List<Produit> parCategorie(String categorie) {
    return produitRepository.findAll().stream()
        .filter(produit -> produit.getCategorie().equals(categorie))
        .toList();
  }
}
