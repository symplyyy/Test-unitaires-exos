Feature: Gestion des comptes bancaires

  Scenario: Creation d'un nouveau compte
    Given aucun compte "ACC-1" n'existe
    When je cree un compte "ACC-1" pour "Alice"
    Then le compte "ACC-1" existe avec un solde de 0

  Scenario: Depot d'argent sur un compte
    Given un compte "ACC-2" pour "Bob" avec un solde de 100
    When je depose 50 sur le compte "ACC-2"
    Then le solde du compte "ACC-2" est de 150

  Scenario: Retrait avec fonds suffisants
    Given un compte "ACC-3" pour "Carol" avec un solde de 100
    When je retire 40 du compte "ACC-3"
    Then le solde du compte "ACC-3" est de 60

  Scenario: Retrait avec fonds insuffisants
    Given un compte "ACC-4" pour "Dan" avec un solde de 30
    When je retire 50 du compte "ACC-4"
    Then une erreur de fonds insuffisants est levee

  Scenario: Virement entre deux comptes
    Given un compte "ACC-5" pour "Eve" avec un solde de 200
    And un compte "ACC-6" pour "Frank" avec un solde de 0
    When je vire 80 du compte "ACC-5" vers le compte "ACC-6"
    Then le solde du compte "ACC-5" est de 120
    And le solde du compte "ACC-6" est de 80

  Scenario: Virement refuse pour solde insuffisant
    Given un compte "ACC-7" pour "Grace" avec un solde de 50
    And un compte "ACC-8" pour "Heidi" avec un solde de 0
    When je vire 100 du compte "ACC-7" vers le compte "ACC-8"
    Then une erreur de fonds insuffisants est levee
