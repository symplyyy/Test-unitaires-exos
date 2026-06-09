# language: fr
Fonctionnalité: Commande refusée
  En tant que boutique
  Je veux refuser les commandes invalides
  Afin de ne pas vendre un produit inexistant ou indisponible

  Scénario: Produit inconnu
    Étant donné aucun produit avec la référence "REF-404"
    Et un client "client@boutique.com" de profil "STANDARD"
    Quand il commande 1 unités du produit "REF-404"
    Alors la commande est refusée
    Et le motif de refus est "produit inconnu"

  Scénario: Stock insuffisant
    Étant donné le produit "REF-001" nommé "Clavier" au prix de 100.0 avec un stock de 3
    Et un client "client@boutique.com" de profil "STANDARD"
    Quand il commande 5 unités du produit "REF-001"
    Alors la commande est refusée
    Et le motif de refus est "stock insuffisant"
