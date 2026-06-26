package com.ynov.bank.bdd;

import com.ynov.bank.dto.CreateAccountRequest;
import com.ynov.bank.exception.InsufficientFundsException;
import com.ynov.bank.model.Account;
import com.ynov.bank.service.AccountService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@CucumberContextConfiguration
@SpringBootTest
public class AccountSteps {

    @Autowired
    private AccountService accountService;

    // on garde l'exception sous la main pour la verifier dans le Then
    private RuntimeException caught;

    @Given("aucun compte {string} n'existe")
    public void aucunCompteNExiste(String number) {
        boolean present = accountService.getAll().stream()
                .anyMatch(a -> a.getNumber().equals(number));
        assertThat(present).isFalse();
    }

    @Given("un compte {string} pour {string} avec un solde de {int}")
    public void unCompteAvecSolde(String number, String owner, int balance) {
        CreateAccountRequest request = new CreateAccountRequest();
        request.setNumber(number);
        request.setOwner(owner);
        accountService.create(request);
        if (balance > 0) {
            accountService.deposit(number, BigDecimal.valueOf(balance));
        }
    }

    @When("je cree un compte {string} pour {string}")
    public void jeCreeUnCompte(String number, String owner) {
        CreateAccountRequest request = new CreateAccountRequest();
        request.setNumber(number);
        request.setOwner(owner);
        accountService.create(request);
    }

    @When("je depose {int} sur le compte {string}")
    public void jeDepose(int amount, String number) {
        accountService.deposit(number, BigDecimal.valueOf(amount));
    }

    @When("je retire {int} du compte {string}")
    public void jeRetire(int amount, String number) {
        try {
            accountService.withdraw(number, BigDecimal.valueOf(amount));
        } catch (RuntimeException e) {
            caught = e;
        }
    }

    @When("je vire {int} du compte {string} vers le compte {string}")
    public void jeVire(int amount, String from, String to) {
        try {
            accountService.transfer(from, to, BigDecimal.valueOf(amount));
        } catch (RuntimeException e) {
            caught = e;
        }
    }

    @Then("le compte {string} existe avec un solde de {int}")
    public void leCompteExisteAvecSolde(String number, int balance) {
        Account account = accountService.getByNumber(number);
        assertThat(account.getBalance()).isEqualByComparingTo(BigDecimal.valueOf(balance));
    }

    @Then("le solde du compte {string} est de {int}")
    public void leSoldeDuCompteEstDe(String number, int balance) {
        Account account = accountService.getByNumber(number);
        assertThat(account.getBalance()).isEqualByComparingTo(BigDecimal.valueOf(balance));
    }

    @Then("une erreur de fonds insuffisants est levee")
    public void uneErreurDeFondsInsuffisants() {
        assertThat(caught).isInstanceOf(InsufficientFundsException.class);
    }
}
