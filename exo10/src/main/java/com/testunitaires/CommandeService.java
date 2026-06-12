package com.testunitaires;

public class CommandeService {
  private final CommandeRepository commandeRepository;
  private final ProduitRepository produitRepository;

  public CommandeService(CommandeRepository commandeRepository, ProduitRepository produitRepository) {
    this.commandeRepository = commandeRepository;
    this.produitRepository = produitRepository;
  }

  public Commande ajouterProduit(String commandeId, String produitId) {
    Commande commande = chargerCommande(commandeId);
    Produit produit = produitRepository.findById(produitId)
        .orElseThrow(() -> new ProduitIntrouvableException("produit inconnu"));
    commande.ajouterProduit(produit);
    commandeRepository.save(commande);
    return commande;
  }

  public Commande retirerProduit(String commandeId, String produitId) {
    Commande commande = chargerCommande(commandeId);
    commande.retirerProduit(produitId);
    commandeRepository.save(commande);
    return commande;
  }

  public ConfirmationCommande valider(String commandeId) {
    Commande commande = chargerCommande(commandeId);
    commande.valider();
    commandeRepository.save(commande);
    return new ConfirmationCommande(commandeId, "Commande validée");
  }

  private Commande chargerCommande(String commandeId) {
    return commandeRepository.findById(commandeId)
        .orElseThrow(() -> new CommandeIntrouvableException("commande inconnue"));
  }
}
