package com.testunitaire;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class GradingCalculatorTest {

    @Test
    void score95_presence90_shouldReturnA() {
        // Arrange
        GradingCalculator calculator = new GradingCalculator(95, 90);

        // Act
        char grade = calculator.getGrade();

        // Assert
        assertEquals('A', grade);
    }

    @Test
    void score85_presence90_shouldReturnB() {
        // Arrange
        GradingCalculator calculator = new GradingCalculator(85, 90);

        // Act
        char grade = calculator.getGrade();

        // Assert
        assertEquals('B', grade);
    }

    @Test
    void score65_presence90_shouldReturnC() {
        // Arrange
        GradingCalculator calculator = new GradingCalculator(65, 90);

        // Act
        char grade = calculator.getGrade();

        // Assert
        assertEquals('C', grade);
    }

    @Test
    void score95_presence65_shouldReturnB() {
        // Arrange
        GradingCalculator calculator = new GradingCalculator(95, 65);

        // Act
        char grade = calculator.getGrade();

        // Assert
        assertEquals('B', grade);
    }

    @Test
    void score95_presence55_shouldReturnF() {
        // Arrange
        GradingCalculator calculator = new GradingCalculator(95, 55);

        // Act
        char grade = calculator.getGrade();

        // Assert
        assertEquals('F', grade);
    }

    @Test
    void score65_presence55_shouldReturnF() {
        // Arrange
        GradingCalculator calculator = new GradingCalculator(65, 55);

        // Act
        char grade = calculator.getGrade();

        // Assert
        assertEquals('F', grade);
    }

    @Test
    void score50_presence90_shouldReturnF() {
        // Arrange
        GradingCalculator calculator = new GradingCalculator(50, 90);

        // Act
        char grade = calculator.getGrade();

        // Assert
        assertEquals('F', grade);
    }
}
