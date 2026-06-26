# API bancaire — TDD / BDD / CI

Petite API REST Spring Boot qui gere des comptes bancaires (creation, depot, retrait, virement).
Le but de l'exo est surtout de pratiquer le TDD, le BDD (Cucumber) et l'integration continue.

## Stack

- Java 17 (la CI GitHub Actions tourne sur Java 21)
- Spring Boot 3.2.5
- JUnit 5 + Mockito (tests unitaires)
- Cucumber 7 (BDD)
- JaCoCo (couverture)
- Stockage en memoire (pas de base de donnees)

## Lancer le projet

```bash
mvn spring-boot:run
```

## Lancer les tests + generer les rapports

```bash
mvn clean verify
```

Les rapports sont ensuite dans `target/` :

- `target/surefire-reports` — resultats des tests
- `target/site/jacoco/index.html` — couverture de code
- `target/cucumber-reports/cucumber.html` (et `.json`) — rapport BDD

Un zip de ces rapports est aussi versionne a la racine du module : [`rapports.zip`](rapports.zip).

## Endpoints

| Methode | URL | Role |
|---|---|---|
| POST | `/accounts` | Creer un compte |
| GET | `/accounts` | Lister les comptes |
| GET | `/accounts/{number}` | Consulter un compte |
| POST | `/accounts/{number}/deposit` | Deposer de l'argent |
| POST | `/accounts/{number}/withdraw` | Retirer de l'argent |
| POST | `/accounts/transfer` | Virement entre deux comptes |

### Codes d'erreur

- `400` montant invalide (<= 0)
- `404` compte introuvable
- `409` numero deja utilise / fonds insuffisants

## Demarche

1. Ecriture des tests d'abord (phase rouge).
2. Implementation du service jusqu'a ce que tout passe au vert.
3. Scenarios Cucumber pour decrire le comportement metier.
4. Rapports JaCoCo + Cucumber.
5. Workflow GitHub Actions (`.github/workflows/ci.yml`) : build, tests et archivage des rapports a chaque push / PR.
