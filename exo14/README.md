# MédiaCity — Gestion des prêts et réservations

Module Java/Maven (sans framework web) qui gère les prêts d'ouvrages d'une médiathèque :
emprunts, retours, pénalités de retard, suspension d'adhérent et réservations.
Développé en TDD pour la logique métier et en BDD (Cucumber) pour les réservations.

## Stack

- Java 17, Maven 3.9+
- JUnit 5 + AssertJ + Mockito (tests unitaires)
- Cucumber 7 (BDD)
- JaCoCo (couverture)
- Stockage en mémoire (repositories `InMemory*`)

## Règles métier

**Prêts**
- un prêt est créé pour un adhérent et un ouvrage ;
- la date de retour est calculée automatiquement (**21 jours**) ;
- un ouvrage déjà emprunté ne peut pas être prêté une seconde fois ;
- pénalité de retard : **0,15 € par jour** ;
- un retard de plus de 7 jours est un « retard important » ; au **3e** dans l'année, l'adhérent est **suspendu** et ne peut plus emprunter.

**Réservations**
- on ne réserve qu'un ouvrage indisponible (déjà emprunté) ;
- plusieurs réservations possibles → file d'attente (ordre d'arrivée) ;
- à la restitution, le premier de la file devient le prochain emprunteur ;
- un adhérent suspendu ne peut pas réserver ;
- un adhérent ne peut pas réserver deux fois le même ouvrage.

## Lancer les tests + rapports

```bash
mvn clean verify
```

- `target/surefire-reports` — résultats des tests
- `target/site/jacoco/index.html` — couverture (le build échoue si le package `service` est sous 80 %)
- `target/cucumber-reports/cucumber.html` (+ `.json`) — rapport BDD

### Site Maven (regroupe les rapports)

```bash
mvn clean verify site
```

Puis ouvrir `target/site/index.html` (→ *Project Reports* : Surefire + JaCoCo).

Un zip des rapports est aussi versionné : [`rapports.zip`](rapports.zip).

## Organisation

```
model/        Member, Book, Loan, Reservation
exception/    exceptions métier
repository/   interfaces + implémentations InMemory
service/      LoanService, ReservationService   <- logique testée
```

## Démarche TDD

Tests écrits d'abord, puis implémentation jusqu'au vert. Couverture actuelle du package
`service` : **100 %**.
