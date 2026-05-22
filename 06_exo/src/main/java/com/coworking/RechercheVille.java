package com.coworking;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RechercheVille {
    private List<String> villes;

    public RechercheVille() {
        this.villes = Arrays.asList(
                "Paris", "Budapest", "Skopje", "Rotterdam", "Valence", "Vancouver",
                "Amsterdam", "Vienne", "Sydney", "New York", "Londres", "Bangkok",
                "Hong Kong", "Dubaï", "Rome", "Istanbul"
        );
    }

    public List<String> Rechercher(String mot) {
        if (mot.equals("*")) {
            return villes;
        }

        if (mot.length() < 2) {
            throw new NotFoundException("Il faut au moins 2 caractères");
        }

        List<String> resultat = new ArrayList<>();
        String recherche = mot.toLowerCase();

        for (String ville : villes) {
            if (ville.toLowerCase().contains(recherche)) {
                resultat.add(ville);
            }
        }

        return resultat;
    }
}
