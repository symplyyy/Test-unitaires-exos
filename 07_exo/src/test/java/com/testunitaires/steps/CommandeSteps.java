package com.testunitaires.steps;

import com.testunitaires.Commande;
import com.testunitaires.CommandeRefuseeException;
import com.testunitaires.CommandeService;
import com.testunitaires.Produit;
import com.testunitaires.ProduitRepository;
import com.testunitaires.Profil;
import com.testunitaires.Recu;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CommandeSteps {

    private final ProduitRepository produitRepository = mock(ProduitRepository.class);
    private final CommandeService service = new CommandeService(produitRepository);

    private Profil profil;
    private String email;

    private Recu recu;
    private CommandeRefuseeException refus;

    @Given("le produit {string} nommé {string} au prix de {double} avec un stock de {int}")
    public void le_produit_existe(String reference, String nom, double prix, int stock) {
        Produit produit = new Produit(reference, nom, prix, stock);
        when(produitRepository.findByReference(reference)).thenReturn(Optional.of(produit));
    }

    @Given("aucun produit avec la référence {string}")
    public void aucun_produit(String reference) {
        when(produitRepository.findByReference(reference)).thenReturn(Optional.empty());
    }

    @Given("un client {string} de profil {string}")
    public void un_client_de_profil(String email, String profil) {
        this.email = email;
        this.profil = Profil.valueOf(profil);
    }

    @When("il commande {int} unités du produit {string}")
    public void il_commande(int quantite, String reference) {
        Commande commande = new Commande(email, reference, quantite);
        try {
            recu = service.passerCommande(commande, profil);
            refus = null;
        } catch (CommandeRefuseeException e) {
            refus = e;
            recu = null;
        }
    }

    @Then("la commande est acceptée")
    public void la_commande_est_acceptee() {
        assertNull(refus);
        assertNotNull(recu);
    }

    @Then("la commande est refusée")
    public void la_commande_est_refusee() {
        assertNull(recu);
        assertNotNull(refus);
    }

    @Then("le reçu indique la référence {string} et la quantité {int}")
    public void le_recu_indique_reference_quantite(String reference, int quantite) {
        assertEquals(reference, recu.getReferenceProduit());
        assertEquals(quantite, recu.getQuantite());
    }

    @Then("le reçu est adressé à {string} pour le produit {string}")
    public void le_recu_est_adresse(String emailAttendu, String nomAttendu) {
        assertEquals(emailAttendu, recu.getEmailClient());
        assertEquals(nomAttendu, recu.getNomProduit());
    }

    @Then("le reçu indique un montant total de {double}")
    public void le_recu_indique_un_montant(double montant) {
        assertEquals(montant, recu.getMontantTotal(), 0.001);
    }

    @Then("le reçu contient le message {string}")
    public void le_recu_contient_le_message(String message) {
        assertEquals(message, recu.getMessage());
    }

    @Then("le motif de refus est {string}")
    public void le_motif_de_refus_est(String motif) {
        assertEquals(motif, refus.getMessage());
    }
}
