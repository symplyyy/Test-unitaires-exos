package com.ynov.booking.bdd;

import com.ynov.booking.dto.CreateReservationRequest;
import com.ynov.booking.dto.CreateRoomRequest;
import com.ynov.booking.exception.OverlappingReservationException;
import com.ynov.booking.exception.RoomNotFoundException;
import com.ynov.booking.model.Reservation;
import com.ynov.booking.model.ReservationStatus;
import com.ynov.booking.model.Room;
import com.ynov.booking.service.ReservationService;
import com.ynov.booking.service.RoomService;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@CucumberContextConfiguration
@SpringBootTest
public class ReservationSteps {

    @Autowired
    private RoomService roomService;

    @Autowired
    private ReservationService reservationService;

    private Room room;
    private Reservation reservation;
    private RuntimeException caught;

    @Given("une salle {string} avec une capacite de {int}")
    public void uneSalle(String name, int capacity) {
        CreateRoomRequest request = new CreateRoomRequest();
        request.setName(name);
        request.setCapacity(capacity);
        room = roomService.create(request);
    }

    @And("une reservation confirmee pour {string} de {string} a {string}")
    public void uneReservationConfirmee(String person, String start, String end) {
        reservationService.create(buildRequest(room.getId(), person, start, end));
    }

    @When("je reserve la salle pour {string} de {string} a {string}")
    public void jeReserveLaSalle(String person, String start, String end) {
        try {
            reservation = reservationService.create(buildRequest(room.getId(), person, start, end));
        } catch (RuntimeException e) {
            caught = e;
        }
    }

    @When("je reserve la salle inexistante {long} pour {string} de {string} a {string}")
    public void jeReserveLaSalleInexistante(Long roomId, String person, String start, String end) {
        try {
            reservation = reservationService.create(buildRequest(roomId, person, start, end));
        } catch (RuntimeException e) {
            caught = e;
        }
    }

    @Then("la reservation est confirmee")
    public void laReservationEstConfirmee() {
        assertThat(reservation).isNotNull();
        assertThat(reservation.getStatus()).isEqualTo(ReservationStatus.CONFIRMED);
    }

    @Then("une erreur d'introuvable est levee")
    public void uneErreurDIntrouvable() {
        assertThat(caught).isInstanceOf(RoomNotFoundException.class);
    }

    @Then("une erreur de conflit est levee")
    public void uneErreurDeConflit() {
        assertThat(caught).isInstanceOf(OverlappingReservationException.class);
    }

    private CreateReservationRequest buildRequest(Long roomId, String person, String start, String end) {
        CreateReservationRequest request = new CreateReservationRequest();
        request.setRoomId(roomId);
        request.setPersonName(person);
        request.setStart(LocalDateTime.parse(start));
        request.setEnd(LocalDateTime.parse(end));
        return request;
    }
}
