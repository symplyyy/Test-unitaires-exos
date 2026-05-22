package com.testunitaire;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests de DiceScore")
public class DiceScoreTest {

    @Mock
    private Ide de;

    private DiceScore diceScore;

    @BeforeEach
    void setUp() {
        diceScore = new DiceScore(de);
    }

    @Test
    @DisplayName("Doit retourner 30 quand les deux dés font 6")
    void shouldReturn30WhenBothDiceRoll6() {
        when(de.getRoll()).thenReturn(6, 6);

        assertEquals(30, diceScore.getScore());
    }

    @Test
    @DisplayName("Doit retourner valeur*2+10 quand les deux dés sont égaux (hors double 6)")
    void shouldReturnDoubleValuePlus10WhenBothDiceAreEqualNotSix() {
        when(de.getRoll()).thenReturn(3, 3);

        assertEquals(16, diceScore.getScore());
    }

    @Test
    @DisplayName("Doit retourner le plus grand score quand les dés sont différents (second plus grand)")
    void shouldReturnHighestScoreWhenDiceAreDifferentSecondHigher() {
        when(de.getRoll()).thenReturn(2, 5);

        assertEquals(5, diceScore.getScore());
    }

    @Test
    @DisplayName("Doit retourner le plus grand score quand les dés sont différents (premier plus grand)")
    void shouldReturnHighestScoreWhenDiceAreDifferentFirstHigher() {
        when(de.getRoll()).thenReturn(5, 2);

        assertEquals(5, diceScore.getScore());
    }
}
