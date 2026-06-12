# language: fr
Fonctionnalité: Gestion du compte
  En tant qu'utilisateur
  Je veux créer un compte et me connecter
  Afin de pouvoir passer des commandes

  Scénario: Inscription réussie
    Quand "alice" s'inscrit avec l'email "alice@boutique.com" et le mot de passe "secret"
    Alors l'inscription est confirmée pour "alice"

  Scénario: Inscription avec un identifiant déjà existant
    Étant donné un compte existant avec l'identifiant "alice"
    Quand "alice" s'inscrit avec l'email "alice@boutique.com" et le mot de passe "secret"
    Alors l'inscription est refusée avec le message "identifiant déjà utilisé"

  Scénario: Connexion réussie
    Étant donné un compte "bob" avec le mot de passe "1234"
    Quand "bob" se connecte avec le mot de passe "1234"
    Alors la connexion réussit et redirige vers "accueil"

  Scénario: Connexion échouée
    Étant donné un compte "bob" avec le mot de passe "1234"
    Quand "bob" se connecte avec le mot de passe "mauvais"
    Alors la connexion échoue avec le message "identifiants invalides"
