Feature: Reservations d'ouvrages a la mediatheque

  Scenario: Reservation d'un ouvrage indisponible
    Given un adherent "Alice"
    And un ouvrage "Dune"
    And l'ouvrage "Dune" est emprunte par "Bob"
    When "Alice" reserve l'ouvrage "Dune"
    Then l'ouvrage "Dune" a 1 reservation

  Scenario: Plusieurs reservations sur le meme ouvrage
    Given un adherent "Alice"
    And un adherent "Carol"
    And un ouvrage "Dune"
    And l'ouvrage "Dune" est emprunte par "Bob"
    When "Alice" reserve l'ouvrage "Dune"
    And "Carol" reserve l'ouvrage "Dune"
    Then l'ouvrage "Dune" a 2 reservations
    And la premiere reservation de l'ouvrage "Dune" est "Alice"

  Scenario: Restitution d'un ouvrage reserve
    Given un adherent "Alice"
    And un ouvrage "Dune"
    And l'ouvrage "Dune" est emprunte par "Bob"
    And "Alice" reserve l'ouvrage "Dune"
    When "Bob" restitue l'ouvrage "Dune"
    Then le prochain emprunteur de l'ouvrage "Dune" est "Alice"

  Scenario: Refus de reservation pour un adherent suspendu
    Given un adherent suspendu "Alice"
    And un ouvrage "Dune"
    And l'ouvrage "Dune" est emprunte par "Bob"
    When "Alice" tente de reserver l'ouvrage "Dune"
    Then la reservation est refusee

  Scenario: Un adherent ne peut pas reserver deux fois le meme ouvrage
    Given un adherent "Alice"
    And un ouvrage "Dune"
    And l'ouvrage "Dune" est emprunte par "Bob"
    And "Alice" reserve l'ouvrage "Dune"
    When "Alice" tente de reserver l'ouvrage "Dune"
    Then la reservation est refusee
