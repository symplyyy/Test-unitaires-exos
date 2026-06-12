package com.testunitaires.steps;

import com.testunitaires.CatalogueService;
import com.testunitaires.Produit;
import com.testunitaires.ProduitRepository;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CatalogueSteps {

  private final ProduitRepository produitRepository = mock(ProduitRepository.class);
  private final CatalogueService catalogueService = new CatalogueService(produitRepository);

  private final List<Produit> catalogue = new ArrayList<>();
  private List<Produit> resultats;

  @Given("le produit {string} dans la catégorie {string} à {double} €")
  public void le_produit(String nom, String categorie, double prix) {
    catalogue.add(new Produit(nom, nom, categorie, prix));
  }

  @When("l'utilisateur recherche le mot-clé {string}")
  public void recherche_mot_cle(String motCle) {
    when(produitRepository.findAll()).thenReturn(catalogue);
    resultats = catalogueService.rechercher(motCle);
  }

  @When("l'utilisateur recherche les produits à moins de {double} €")
  public void recherche_prix_max(double prixMax) {
    when(produitRepository.findAll()).thenReturn(catalogue);
    resultats = catalogueService.rechercherParPrixMax(prixMax);
  }

  @When("l'utilisateur sélectionne la catégorie {string}")
  public void selectionne_categorie(String categorie) {
    when(produitRepository.findAll()).thenReturn(catalogue);
    resultats = catalogueService.parCategorie(categorie);
  }

  @Then("les résultats contiennent {string}")
  public void resultats_contiennent(String nom) {
    assertTrue(resultats.stream().anyMatch(produit -> produit.getNom().equals(nom)));
  }

  @Then("les résultats ne contiennent pas {string}")
  public void resultats_ne_contiennent_pas(String nom) {
    assertTrue(resultats.stream().noneMatch(produit -> produit.getNom().equals(nom)));
  }

  @Then("le nombre de résultats est {int}")
  public void nombre_de_resultats(int nombre) {
    assertEquals(nombre, resultats.size());
  }
}
