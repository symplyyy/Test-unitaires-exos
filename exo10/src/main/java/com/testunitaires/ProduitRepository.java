package com.testunitaires;

import java.util.List;
import java.util.Optional;

public interface ProduitRepository {
  List<Produit> findAll();

  Optional<Produit> findById(String id);
}
