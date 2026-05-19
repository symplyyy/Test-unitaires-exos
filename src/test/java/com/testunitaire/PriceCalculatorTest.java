package com.testunitaire;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PriceCalculatorTest {

    // Cas nominaux

    @Test
    void calculateTotalPrice_shouldReturn30() {
        // Arrange

        PriceCalculator priceCalculator = new PriceCalculator();

        // Act

        double result = priceCalculator.calculateTotalPrice(10.0, 3);

        // Assert

        assertEquals(30, result);
    }

    @Test
    void applyDiscount_shouldReturn80() {
        // Arrange
        PriceCalculator calculator = new PriceCalculator();

        // Act
        double result = calculator.applyDiscount(100.0, 0.20);

        // Assert
        assertEquals(80.0, result);
    }

    @Test
    void calculateVat_shouldReturn20() {
        // Arrange
        PriceCalculator calculator = new PriceCalculator();

        // Act
        double result = calculator.calculateVat(100.0, 0.20);

        // Assert
        assertEquals(20.0, result);
    }

    @Test
    void calculatePriceWithVat_shouldReturn120() {
        // Arrange
        PriceCalculator calculator = new PriceCalculator();

        // Act
        double result = calculator.calculatePriceWithVat(100.0, 0.20);

        // Assert
        assertEquals(120.0, result);
    }

    // Cas d'erreur

    @Test
    void calculateTotalPrice_negativeUnitPrice_shouldThrow() {
        // Arrange
        PriceCalculator calculator = new PriceCalculator();

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> calculator.calculateTotalPrice(-1.0, 3));
    }

    @Test
    void calculateTotalPrice_negativeQuantity_shouldThrow() {
        // Arrange
        PriceCalculator calculator = new PriceCalculator();

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> calculator.calculateTotalPrice(10.0, -1));
    }

    @Test
    void applyDiscount_negativeRate_shouldThrow() {
        // Arrange
        PriceCalculator calculator = new PriceCalculator();

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> calculator.applyDiscount(100.0, -0.20));
    }

    @Test
    void calculateVat_negativeRate_shouldThrow() {
        // Arrange
        PriceCalculator calculator = new PriceCalculator();

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> calculator.calculateVat(100.0, -0.20));
    }

    @Test
    void calculatePriceWithVat_negativeRate_shouldThrow() {
        // Arrange
        PriceCalculator calculator = new PriceCalculator();

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> calculator.calculatePriceWithVat(100.0, -0.20));
    }
}
