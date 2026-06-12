# language: fr
Fonctionnalité: Gestion de la commande
  En tant qu'utilisateur
  Je veux gérer les produits de ma commande
  Afin de préparer puis valider mon achat

  Scénario: Ajouter un produit à une commande
    Étant donné une commande "C1"
    Et le produit "P1" existe
    Quand l'utilisateur ajoute le produit "P1" à la commande "C1"
    Alors la commande contient 1 fois le produit "P1"

  Scénario: Ajouter un produit déjà présent augmente la quantité
    Étant donné une commande "C1"
    Et le produit "P1" existe
    Et la commande "C1" contient 1 fois le produit "P1"
    Quand l'utilisateur ajoute le produit "P1" à la commande "C1"
    Alors la commande contient 2 fois le produit "P1"

  Scénario: Ajouter un produit à une commande inexistante
    Étant donné aucune commande "C9" n'existe
    Et le produit "P1" existe
    Quand l'utilisateur ajoute le produit "P1" à la commande "C9"
    Alors une erreur "commande inconnue" est renvoyée

  Scénario: Diminuer la quantité d'un produit
    Étant donné une commande "C1"
    Et la commande "C1" contient 2 fois le produit "P1"
    Quand l'utilisateur retire le produit "P1" de la commande "C1"
    Alors la commande contient 1 fois le produit "P1"

  Scénario: Retirer le dernier exemplaire supprime le produit
    Étant donné une commande "C1"
    Et la commande "C1" contient 1 fois le produit "P1"
    Quand l'utilisateur retire le produit "P1" de la commande "C1"
    Alors la commande ne contient pas le produit "P1"

  Scénario: Retirer un produit absent de la commande
    Étant donné une commande "C1"
    Quand l'utilisateur retire le produit "P1" de la commande "C1"
    Alors une erreur "produit absent de la commande" est renvoyée

  Scénario: Retirer un produit d'une commande inexistante
    Étant donné aucune commande "C9" n'existe
    Quand l'utilisateur retire le produit "P1" de la commande "C9"
    Alors une erreur "commande inconnue" est renvoyée

  Scénario: Valider une commande
    Étant donné une commande "C1"
    Quand l'utilisateur valide la commande "C1"
    Alors la commande est validée avec le message "Commande validée"

  Scénario: Valider une commande inexistante
    Étant donné aucune commande "C9" n'existe
    Quand l'utilisateur valide la commande "C9"
    Alors une erreur "commande inconnue" est renvoyée
