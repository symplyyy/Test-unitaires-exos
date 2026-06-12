package com.ynov.tickets.bdd;

import com.ynov.tickets.dto.CreateTicketRequest;
import com.ynov.tickets.exception.InvalidStatusTransitionException;
import com.ynov.tickets.exception.TicketNotFoundException;
import com.ynov.tickets.model.Priority;
import com.ynov.tickets.model.Status;
import com.ynov.tickets.model.Ticket;
import com.ynov.tickets.service.TicketService;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@CucumberContextConfiguration
@SpringBootTest
public class TicketSteps {

    @Autowired
    private TicketService service;

    private CreateTicketRequest request;
    private Ticket ticket;
    private RuntimeException caught;

    @Given("un titre {string} et une priorite {string}")
    public void unTitreEtUnePriorite(String title, String priority) {
        request = new CreateTicketRequest();
        request.setTitle(title);
        request.setPriority(Priority.valueOf(priority));
    }

    @When("je cree le ticket")
    public void jeCreeLeTicket() {
        ticket = service.create(request);
    }

    @Then("le ticket est cree avec le statut {string}")
    public void leTicketEstCreeAvecLeStatut(String status) {
        assertThat(ticket.getId()).isNotNull();
        assertThat(ticket.getStatus()).isEqualTo(Status.valueOf(status));
    }

    @Given("un ticket ouvert avec le titre {string} et la priorite {string}")
    public void unTicketOuvert(String title, String priority) {
        CreateTicketRequest req = new CreateTicketRequest();
        req.setTitle(title);
        req.setPriority(Priority.valueOf(priority));
        ticket = service.create(req);
    }

    @When("je passe son statut a {string}")
    public void jePasseSonStatutA(String status) {
        ticket = service.updateStatus(ticket.getId(), Status.valueOf(status));
    }

    @Then("le statut du ticket est {string}")
    public void leStatutDuTicketEst(String status) {
        assertThat(ticket.getStatus()).isEqualTo(Status.valueOf(status));
    }

    @And("son statut est passe a {string}")
    public void sonStatutEstPasseA(String status) {
        ticket = service.updateStatus(ticket.getId(), Status.valueOf(status));
    }

    @When("je tente de passer son statut a {string}")
    public void jeTenteDePasserSonStatutA(String status) {
        try {
            service.updateStatus(ticket.getId(), Status.valueOf(status));
        } catch (RuntimeException e) {
            caught = e;
        }
    }

    @Then("une erreur de conflit est levee")
    public void uneErreurDeConflitEstLevee() {
        assertThat(caught).isInstanceOf(InvalidStatusTransitionException.class);
    }

    @When("je consulte le ticket numero {long}")
    public void jeConsulteLeTicketNumero(Long id) {
        try {
            service.getById(id);
        } catch (RuntimeException e) {
            caught = e;
        }
    }

    @Then("une erreur d'introuvable est levee")
    public void uneErreurDIntrouvableEstLevee() {
        assertThat(caught).isInstanceOf(TicketNotFoundException.class);
    }
}
