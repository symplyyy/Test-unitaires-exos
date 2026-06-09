# language: fr
Fonctionnalité: Réservation acceptée
  En tant qu'employé
  Je veux réserver une salle libre adaptée à mon besoin
  Afin de recevoir une confirmation

  Contexte:
    Étant donné la salle "S1" nommée "Atlas" d'une capacité de 10

  Scénario: Réservation dans un créneau libre
    Quand "alice@boite.com" réserve la salle "S1" pour 5 participants du "2026-06-10T09:00" au "2026-06-10T10:00"
    Alors la réservation est acceptée
    Et la confirmation porte la salle "S1" nommée "Atlas" pour 5 participants
    Et la confirmation couvre le créneau "2026-06-10T09:00" - "2026-06-10T10:00"
    Et la confirmation est destinée à "alice@boite.com" avec le message "Réservation confirmée"

  Scénario: Réservation à la capacité maximale
    Quand "alice@boite.com" réserve la salle "S1" pour 10 participants du "2026-06-10T09:00" au "2026-06-10T10:00"
    Alors la réservation est acceptée

  Scénario: Créneau commençant juste après une réservation existante
    Étant donné une réservation existante sur la salle "S1" du "2026-06-10T08:00" au "2026-06-10T09:00"
    Quand "alice@boite.com" réserve la salle "S1" pour 5 participants du "2026-06-10T09:00" au "2026-06-10T10:00"
    Alors la réservation est acceptée

  Scénario: Créneau se terminant avant une réservation existante
    Étant donné une réservation existante sur la salle "S1" du "2026-06-10T11:00" au "2026-06-10T12:00"
    Quand "alice@boite.com" réserve la salle "S1" pour 5 participants du "2026-06-10T09:00" au "2026-06-10T10:00"
    Alors la réservation est acceptée

  Scénario: Une confirmation est envoyée en cas de succès
    Quand "alice@boite.com" réserve la salle "S1" pour 5 participants du "2026-06-10T09:00" au "2026-06-10T10:00"
    Alors la réservation est acceptée
    Et une confirmation est envoyée à "alice@boite.com"
