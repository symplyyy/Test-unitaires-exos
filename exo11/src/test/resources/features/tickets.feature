Feature: Gestion des tickets de support

  Scenario: Creation d'un ticket valide
    Given un titre "Imprimante en panne" et une priorite "HIGH"
    When je cree le ticket
    Then le ticket est cree avec le statut "OPEN"

  Scenario: Resolution d'un ticket
    Given un ticket ouvert avec le titre "Souris cassee" et la priorite "LOW"
    When je passe son statut a "RESOLVED"
    Then le statut du ticket est "RESOLVED"

  Scenario: Refus de modification d'un ticket deja resolu
    Given un ticket ouvert avec le titre "Clavier HS" et la priorite "MEDIUM"
    And son statut est passe a "RESOLVED"
    When je tente de passer son statut a "IN_PROGRESS"
    Then une erreur de conflit est levee

  Scenario: Consultation d'un ticket inexistant
    When je consulte le ticket numero 9999
    Then une erreur d'introuvable est levee
