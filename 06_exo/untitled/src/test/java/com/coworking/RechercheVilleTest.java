package com.coworking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Tests de RechercheVille")
public class RechercheVilleTest {

    private RechercheVille rechercheVille;

    @BeforeEach
    void setUp() {
        rechercheVille = new RechercheVille();
    }

    @Test
    @DisplayName("Doit lever NotFoundException quand le texte fait moins de 2 caractères")
    void shouldThrowNotFoundExceptionWhenSearchTextIsTooShort() {
        assertThrows(NotFoundException.class, () -> rechercheVille.Rechercher("a"));
    }

    @Test
    @DisplayName("Doit lever NotFoundException quand le texte est vide")
    void shouldThrowNotFoundExceptionWhenSearchTextIsEmpty() {
        assertThrows(NotFoundException.class, () -> rechercheVille.Rechercher(""));
    }

    @Test
    @DisplayName("Doit retourner les villes commençant par le texte de recherche")
    void shouldReturnCitiesStartingWithSearchText() {
        List<String> resultat = rechercheVille.Rechercher("Va");

        assertEquals(2, resultat.size());
        assertTrue(resultat.contains("Valence"));
        assertTrue(resultat.contains("Vancouver"));
    }

    @Test
    @DisplayName("Doit être insensible à la casse")
    void shouldBeCaseInsensitive() {
        List<String> resultat = rechercheVille.Rechercher("va");

        assertEquals(2, resultat.size());
        assertTrue(resultat.contains("Valence"));
        assertTrue(resultat.contains("Vancouver"));
    }

    @Test
    @DisplayName("Doit trouver les villes contenant le texte de recherche (sous-chaîne)")
    void shouldFindCitiesContainingSubstring() {
        List<String> resultat = rechercheVille.Rechercher("ape");

        assertEquals(1, resultat.size());
        assertTrue(resultat.contains("Budapest"));
    }

    @Test
    @DisplayName("Doit retourner toutes les villes quand le texte est *")
    void shouldReturnAllCitiesWhenSearchIsAsterisk() {
        List<String> resultat = rechercheVille.Rechercher("*");

        assertEquals(16, resultat.size());
    }
}
