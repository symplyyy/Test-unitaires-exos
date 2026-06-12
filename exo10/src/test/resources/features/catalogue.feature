# language: fr
Fonctionnalité: Recherche et navigation dans le catalogue
  En tant qu'utilisateur
  Je veux rechercher et parcourir les produits
  Afin de trouver rapidement ce dont j'ai besoin

  Contexte:
    Étant donné le produit "Clavier" dans la catégorie "Informatique" à 30.0 €
    Et le produit "Souris" dans la catégorie "Informatique" à 20.0 €
    Et le produit "Roman" dans la catégorie "Livres" à 15.0 €

  Scénario: Recherche par mot-clé
    Quand l'utilisateur recherche le mot-clé "Clavier"
    Alors les résultats contiennent "Clavier"
    Et les résultats ne contiennent pas "Souris"

  Scénario: Recherche par prix maximum
    Quand l'utilisateur recherche les produits à moins de 20.0 €
    Alors les résultats contiennent "Souris"
    Et les résultats contiennent "Roman"
    Et les résultats ne contiennent pas "Clavier"

  Scénario: Navigation par catégorie
    Quand l'utilisateur sélectionne la catégorie "Informatique"
    Alors le nombre de résultats est 2
    Et les résultats contiennent "Clavier"
    Et les résultats contiennent "Souris"
