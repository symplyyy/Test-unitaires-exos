package com.testunitaires;

import java.util.Optional;

public interface UtilisateurRepository {
  Optional<Utilisateur> findByUsername(String username);

  void save(Utilisateur utilisateur);
}
