package com.ynov.mediacity.bdd;

import com.ynov.mediacity.model.Book;
import com.ynov.mediacity.model.Loan;
import com.ynov.mediacity.model.Member;
import com.ynov.mediacity.repository.BookRepository;
import com.ynov.mediacity.repository.LoanRepository;
import com.ynov.mediacity.repository.MemberRepository;
import com.ynov.mediacity.repository.ReservationRepository;
import com.ynov.mediacity.repository.memory.InMemoryBookRepository;
import com.ynov.mediacity.repository.memory.InMemoryLoanRepository;
import com.ynov.mediacity.repository.memory.InMemoryMemberRepository;
import com.ynov.mediacity.repository.memory.InMemoryReservationRepository;
import com.ynov.mediacity.service.LoanService;
import com.ynov.mediacity.service.ReservationService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

// Cucumber cree une instance neuve par scenario : tout repart de zero a chaque fois.
public class ReservationSteps {

    private final MemberRepository memberRepository = new InMemoryMemberRepository();
    private final BookRepository bookRepository = new InMemoryBookRepository();
    private final LoanRepository loanRepository = new InMemoryLoanRepository();
    private final ReservationRepository reservationRepository = new InMemoryReservationRepository();

    private final LoanService loanService =
            new LoanService(memberRepository, bookRepository, loanRepository);
    private final ReservationService reservationService =
            new ReservationService(memberRepository, bookRepository, loanRepository, reservationRepository);

    private final LocalDate loanDay = LocalDate.of(2026, 1, 1);
    private int reservationCount;
    private RuntimeException caught;

    @Given("un adherent {string}")
    public void unAdherent(String name) {
        memberRepository.save(new Member(name, name));
    }

    @Given("un adherent suspendu {string}")
    public void unAdherentSuspendu(String name) {
        Member member = new Member(name, name);
        member.suspend();
        memberRepository.save(member);
    }

    @Given("un ouvrage {string}")
    public void unOuvrage(String title) {
        bookRepository.save(new Book(title, title));
    }

    @Given("l'ouvrage {string} est emprunte par {string}")
    public void louvrageEstEmpruntePar(String title, String borrower) {
        // on s'assure que l'emprunteur existe puis on cree le pret
        if (memberRepository.findById(borrower).isEmpty()) {
            memberRepository.save(new Member(borrower, borrower));
        }
        loanService.lend(borrower, title, loanDay);
    }

    @When("{string} reserve l'ouvrage {string}")
    public void reserve(String member, String title) {
        reservationService.reserve(member, title, LocalDateTime.now());
    }

    @When("{string} tente de reserver l'ouvrage {string}")
    public void tenteDeReserver(String member, String title) {
        try {
            reservationService.reserve(member, title, LocalDateTime.now());
        } catch (RuntimeException e) {
            caught = e;
        }
    }

    @When("{string} restitue l'ouvrage {string}")
    public void restitue(String member, String title) {
        Loan active = loanRepository.findActiveLoanForBook(title).orElseThrow();
        // rendu a temps, pas de penalite
        loanService.returnBook(active.getId(), loanDay.plusDays(5));
    }

    @Then("l'ouvrage {string} a {int} reservation(s)")
    public void louvrageAReservations(String title, int expected) {
        reservationCount = reservationRepository.findByBookInOrder(title).size();
        assertThat(reservationCount).isEqualTo(expected);
    }

    @Then("la premiere reservation de l'ouvrage {string} est {string}")
    public void laPremiereReservationEst(String title, String member) {
        assertThat(reservationService.nextReservation(title)).isPresent();
        assertThat(reservationService.nextReservation(title).get().getMemberId()).isEqualTo(member);
    }

    @Then("le prochain emprunteur de l'ouvrage {string} est {string}")
    public void leProchainEmprunteurEst(String title, String member) {
        assertThat(reservationService.nextReservation(title)).isPresent();
        assertThat(reservationService.nextReservation(title).get().getMemberId()).isEqualTo(member);
    }

    @Then("la reservation est refusee")
    public void laReservationEstRefusee() {
        assertThat(caught).isNotNull();
    }
}
