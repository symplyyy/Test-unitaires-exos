package com.testunitaires;

import java.util.Optional;

public class CommandeService {

    private final ProduitRepository produitRepository;

    public CommandeService(ProduitRepository produitRepository) {
        this.produitRepository = produitRepository;
    }

    public Recu passerCommande(Commande commande, Profil profil) {
        Optional<Produit> produitOptionnel =
                produitRepository.findByReference(commande.getReferenceProduit());

        if (produitOptionnel.isEmpty()) {
            throw new CommandeRefuseeException("produit inconnu");
        }

        Produit produit = produitOptionnel.get();

        if (commande.getQuantite() > produit.getStockDisponible()) {
            throw new CommandeRefuseeException("stock insuffisant");
        }

        double montantTotal = calculerMontant(produit, commande.getQuantite(), profil);

        return new Recu(
                commande.getEmailClient(),
                produit.getReference(),
                produit.getNom(),
                commande.getQuantite(),
                montantTotal,
                "Commande confirmée");
    }

    private double calculerMontant(Produit produit, int quantite, Profil profil) {
        double brut = produit.getPrixUnitaire() * quantite;
        double net = brut * (1 - profil.getRemise());
        return Math.round(net * 100.0) / 100.0;
    }
}
