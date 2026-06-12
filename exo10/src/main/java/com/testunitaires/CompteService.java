package com.testunitaires;

public class CompteService {
  private final UtilisateurRepository utilisateurRepository;

  public CompteService(UtilisateurRepository utilisateurRepository) {
    this.utilisateurRepository = utilisateurRepository;
  }

  public ConfirmationInscription inscrire(String email, String username, String password) {
    if (utilisateurRepository.findByUsername(username).isPresent()) {
      throw new CompteExistantException("identifiant déjà utilisé");
    }
    utilisateurRepository.save(new Utilisateur(email, username, password));
    return new ConfirmationInscription(username, "Compte créé");
  }

  public Session seConnecter(String username, String password) {
    Utilisateur utilisateur = utilisateurRepository.findByUsername(username)
        .filter(u -> u.getPassword().equals(password))
        .orElseThrow(() -> new ConnexionException("identifiants invalides"));
    return new Session(utilisateur.getUsername(), "accueil");
  }
}
