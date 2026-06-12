package com.testunitaires;

import java.util.Optional;

public interface CommandeRepository {
  Optional<Commande> findById(String id);

  void save(Commande commande);
}
