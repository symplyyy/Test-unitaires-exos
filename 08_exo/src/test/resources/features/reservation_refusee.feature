# language: fr
Fonctionnalité: Réservation refusée
  En tant qu'entreprise
  Je veux refuser les réservations invalides
  Afin de garantir la cohérence du planning des salles

  Contexte:
    Étant donné la salle "S1" nommée "Atlas" d'une capacité de 10

  Scénario: Salle inconnue
    Quand "alice@boite.com" réserve la salle "S404" pour 5 participants du "2026-06-10T09:00" au "2026-06-10T10:00"
    Alors la réservation est refusée
    Et le motif de refus est "salle inconnue"

  Scénario: Capacité insuffisante
    Quand "alice@boite.com" réserve la salle "S1" pour 11 participants du "2026-06-10T09:00" au "2026-06-10T10:00"
    Alors la réservation est refusée
    Et le motif de refus est "capacité insuffisante"

  Scénario: Période invalide
    Quand "alice@boite.com" réserve la salle "S1" pour 5 participants du "2026-06-10T10:00" au "2026-06-10T09:00"
    Alors la réservation est refusée
    Et le motif de refus est "période invalide"

  Scénario: Conflit avec une réservation existante
    Étant donné une réservation existante sur la salle "S1" du "2026-06-10T09:00" au "2026-06-10T10:00"
    Quand "alice@boite.com" réserve la salle "S1" pour 5 participants du "2026-06-10T09:30" au "2026-06-10T10:30"
    Alors la réservation est refusée
    Et le motif de refus est "salle déjà réservée"

  Scénario: Aucune confirmation n'est envoyée en cas d'échec
    Quand "alice@boite.com" réserve la salle "S404" pour 5 participants du "2026-06-10T09:00" au "2026-06-10T10:00"
    Alors la réservation est refusée
    Et aucune confirmation n'est envoyée
