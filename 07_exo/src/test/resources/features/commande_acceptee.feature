# language: fr
Fonctionnalité: Commande acceptée
  En tant que client de la boutique
  Je veux commander un produit disponible en stock
  Afin de recevoir un reçu avec le montant remisé selon mon profil

  Contexte:
    Étant donné le produit "REF-001" nommé "Clavier" au prix de 100.0 avec un stock de 10

  Plan du scénario: La remise dépend du profil client
    Étant donné un client "client@boutique.com" de profil "<profil>"
    Quand il commande 2 unités du produit "REF-001"
    Alors la commande est acceptée
    Et le reçu indique la référence "REF-001" et la quantité 2
    Et le reçu est adressé à "client@boutique.com" pour le produit "Clavier"
    Et le reçu indique un montant total de <total>
    Et le reçu contient le message "Commande confirmée"

    Exemples:
      | profil   | total |
      | STANDARD | 200.0 |
      | PREMIUM  | 180.0 |
      | VIP      | 160.0 |
