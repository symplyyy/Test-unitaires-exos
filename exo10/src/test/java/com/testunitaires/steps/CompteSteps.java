package com.testunitaires.steps;

import com.testunitaires.CompteExistantException;
import com.testunitaires.CompteService;
import com.testunitaires.ConfirmationInscription;
import com.testunitaires.ConnexionException;
import com.testunitaires.Session;
import com.testunitaires.Utilisateur;
import com.testunitaires.UtilisateurRepository;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CompteSteps {

  private final UtilisateurRepository utilisateurRepository = mock(UtilisateurRepository.class);
  private final CompteService compteService = new CompteService(utilisateurRepository);

  private ConfirmationInscription confirmation;
  private CompteExistantException inscriptionRefusee;
  private Session session;
  private ConnexionException connexionRefusee;

  @Given("un compte existant avec l'identifiant {string}")
  public void un_compte_existant(String username) {
    when(utilisateurRepository.findByUsername(username))
        .thenReturn(Optional.of(new Utilisateur(username + "@boutique.com", username, "secret")));
  }

  @Given("un compte {string} avec le mot de passe {string}")
  public void un_compte_avec_mot_de_passe(String username, String password) {
    when(utilisateurRepository.findByUsername(username))
        .thenReturn(Optional.of(new Utilisateur(username + "@boutique.com", username, password)));
  }

  @When("{string} s'inscrit avec l'email {string} et le mot de passe {string}")
  public void s_inscrit(String username, String email, String password) {
    try {
      confirmation = compteService.inscrire(email, username, password);
      inscriptionRefusee = null;
    } catch (CompteExistantException e) {
      inscriptionRefusee = e;
      confirmation = null;
    }
  }

  @When("{string} se connecte avec le mot de passe {string}")
  public void se_connecte(String username, String password) {
    try {
      session = compteService.seConnecter(username, password);
      connexionRefusee = null;
    } catch (ConnexionException e) {
      connexionRefusee = e;
      session = null;
    }
  }

  @Then("l'inscription est confirmée pour {string}")
  public void inscription_confirmee(String username) {
    assertNotNull(confirmation);
    assertEquals(username, confirmation.getUsername());
  }

  @Then("l'inscription est refusée avec le message {string}")
  public void inscription_refusee(String message) {
    assertNotNull(inscriptionRefusee);
    assertEquals(message, inscriptionRefusee.getMessage());
  }

  @Then("la connexion réussit et redirige vers {string}")
  public void connexion_reussit(String page) {
    assertNotNull(session);
    assertEquals(page, session.getPage());
  }

  @Then("la connexion échoue avec le message {string}")
  public void connexion_echoue(String message) {
    assertNotNull(connexionRefusee);
    assertEquals(message, connexionRefusee.getMessage());
  }
}
