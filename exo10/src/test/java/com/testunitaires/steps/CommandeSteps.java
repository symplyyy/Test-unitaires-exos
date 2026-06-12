package com.testunitaires.steps;

import com.testunitaires.Commande;
import com.testunitaires.CommandeRepository;
import com.testunitaires.CommandeService;
import com.testunitaires.ConfirmationCommande;
import com.testunitaires.Produit;
import com.testunitaires.ProduitRepository;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CommandeSteps {

  private final CommandeRepository commandeRepository = mock(CommandeRepository.class);
  private final ProduitRepository produitRepository = mock(ProduitRepository.class);
  private final CommandeService commandeService =
      new CommandeService(commandeRepository, produitRepository);

  private Commande commande;
  private ConfirmationCommande confirmation;
  private RuntimeException erreur;

  @Given("une commande {string}")
  public void une_commande(String commandeId) {
    commande = new Commande(commandeId);
    when(commandeRepository.findById(commandeId)).thenReturn(Optional.of(commande));
  }

  @Given("aucune commande {string} n'existe")
  public void aucune_commande(String commandeId) {
    when(commandeRepository.findById(commandeId)).thenReturn(Optional.empty());
  }

  @Given("le produit {string} existe")
  public void le_produit_existe(String produitId) {
    when(produitRepository.findById(produitId)).thenReturn(Optional.of(produit(produitId)));
  }

  @Given("la commande {string} contient {int} fois le produit {string}")
  public void la_commande_contient(String commandeId, int quantite, String produitId) {
    for (int i = 0; i < quantite; i++) {
      commande.ajouterProduit(produit(produitId));
    }
  }

  @When("l'utilisateur ajoute le produit {string} à la commande {string}")
  public void ajoute_produit(String produitId, String commandeId) {
    executer(() -> commande = commandeService.ajouterProduit(commandeId, produitId));
  }

  @When("l'utilisateur retire le produit {string} de la commande {string}")
  public void retire_produit(String produitId, String commandeId) {
    executer(() -> commande = commandeService.retirerProduit(commandeId, produitId));
  }

  @When("l'utilisateur valide la commande {string}")
  public void valide_commande(String commandeId) {
    executer(() -> confirmation = commandeService.valider(commandeId));
  }

  @Then("la commande contient {int} fois le produit {string}")
  public void la_commande_contient_apres(int quantite, String produitId) {
    assertEquals(quantite, commande.quantite(produitId));
  }

  @Then("la commande ne contient pas le produit {string}")
  public void la_commande_ne_contient_pas(String produitId) {
    assertEquals(0, commande.quantite(produitId));
  }

  @Then("la commande est validée avec le message {string}")
  public void la_commande_est_validee(String message) {
    assertNotNull(confirmation);
    assertEquals(message, confirmation.getMessage());
  }

  @Then("une erreur {string} est renvoyée")
  public void une_erreur_est_renvoyee(String message) {
    assertNotNull(erreur);
    assertEquals(message, erreur.getMessage());
  }

  private Produit produit(String produitId) {
    return new Produit(produitId, produitId, "Divers", 10.0);
  }

  private void executer(Runnable action) {
    try {
      action.run();
      erreur = null;
    } catch (RuntimeException e) {
      erreur = e;
    }
  }
}
