package com.testunitaire;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FibTest {

    // Range 1

    @Test
    void range1_resultShouldNotBeEmpty() {
        // Arrange
        Fib fib = new Fib(1);

        // Act
        List<Integer> result = fib.getFibSeries();

        // Assert
        assertFalse(result.isEmpty());
    }

    @Test
    void range1_resultShouldContainOnly0() {
        // Arrange
        Fib fib = new Fib(1);

        // Act
        List<Integer> result = fib.getFibSeries();

        // Assert
        assertEquals(Arrays.asList(0), result);
    }

    // Range 6

    @Test
    void range6_resultShouldContain3() {
        // Arrange
        Fib fib = new Fib(6);

        // Act
        List<Integer> result = fib.getFibSeries();

        // Assert
        assertTrue(result.contains(3));
    }

    @Test
    void range6_resultShouldHave6Elements() {
        // Arrange
        Fib fib = new Fib(6);

        // Act
        List<Integer> result = fib.getFibSeries();

        // Assert
        assertEquals(6, result.size());
    }

    @Test
    void range6_resultShouldNotContain4() {
        // Arrange
        Fib fib = new Fib(6);

        // Act
        List<Integer> result = fib.getFibSeries();

        // Assert
        assertFalse(result.contains(4));
    }

    @Test
    void range6_resultShouldBe0_1_1_2_3_5() {
        // Arrange
        Fib fib = new Fib(6);

        // Act
        List<Integer> result = fib.getFibSeries();

        // Assert
        assertEquals(Arrays.asList(0, 1, 1, 2, 3, 5), result);
    }

    @Test
    void range6_resultShouldBeSortedAscending() {
        // Arrange
        Fib fib = new Fib(6);

        // Act
        List<Integer> result = fib.getFibSeries();

        // Assert
        for (int i = 0; i < result.size() - 1; i++) {
            assertTrue(result.get(i) <= result.get(i + 1));
        }
    }
}
