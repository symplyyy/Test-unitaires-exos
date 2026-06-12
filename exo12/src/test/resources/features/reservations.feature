Feature: Reservation de salles de reunion

  Scenario: Reservation acceptee quand la salle existe et le creneau est libre
    Given une salle "Salle A" avec une capacite de 10
    When je reserve la salle pour "Timeo" de "2026-06-12T10:00:00" a "2026-06-12T11:00:00"
    Then la reservation est confirmee

  Scenario: Reservation refusee quand la salle n'existe pas
    When je reserve la salle inexistante 999 pour "Timeo" de "2026-06-12T10:00:00" a "2026-06-12T11:00:00"
    Then une erreur d'introuvable est levee

  Scenario: Reservation refusee quand le creneau chevauche une reservation existante
    Given une salle "Salle B" avec une capacite de 5
    And une reservation confirmee pour "Bob" de "2026-06-12T10:00:00" a "2026-06-12T11:00:00"
    When je reserve la salle pour "Timeo" de "2026-06-12T10:30:00" a "2026-06-12T11:30:00"
    Then une erreur de conflit est levee
