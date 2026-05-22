package com.testunitaire;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class PasswordValidatorTest {

    // ── Tests classiques ────────────────────────────────────────────────────

    @Test
    void password1_shouldBeValid() {
        // Arrange
        PasswordValidator validator = new PasswordValidator();

        // Act
        boolean result = validator.isValid("Password1!");

        // Assert
        assertTrue(result);
    }

    @Test
    void admin2024_shouldBeValid() {
        // Arrange
        PasswordValidator validator = new PasswordValidator();

        // Act
        boolean result = validator.isValid("Admin2024@");

        // Assert
        assertTrue(result);
    }

    @Test
    void tooShort_shouldBeInvalid() {
        // Arrange
        PasswordValidator validator = new PasswordValidator();

        // Act
        boolean result = validator.isValid("short1!");

        // Assert
        assertFalse(result);
    }

    @Test
    void noLowercase_shouldBeInvalid() {
        // Arrange
        PasswordValidator validator = new PasswordValidator();

        // Act
        boolean result = validator.isValid("PASSWORD1!");

        // Assert
        assertFalse(result);
    }

    @Test
    void noUppercase_shouldBeInvalid() {
        // Arrange
        PasswordValidator validator = new PasswordValidator();

        // Act
        boolean result = validator.isValid("password1!");

        // Assert
        assertFalse(result);
    }

    @Test
    void noDigit_shouldBeInvalid() {
        // Arrange
        PasswordValidator validator = new PasswordValidator();

        // Act
        boolean result = validator.isValid("Password!");

        // Assert
        assertFalse(result);
    }

    @Test
    void noSpecialChar_shouldBeInvalid() {
        // Arrange
        PasswordValidator validator = new PasswordValidator();

        // Act
        boolean result = validator.isValid("Password1");

        // Assert
        assertFalse(result);
    }

    @Test
    void nullPassword_shouldBeInvalid() {
        // Arrange
        PasswordValidator validator = new PasswordValidator();

        // Act
        boolean result = validator.isValid(null);

        // Assert
        assertFalse(result);
    }

    // ── Tests paramétrés @CsvSource ─────────────────────────────────────────

    @ParameterizedTest
    @CsvSource({
        "Password1!, true",
        "Admin2024@, true",
        "short1!,    false",
        "PASSWORD1!, false",
        "password1!, false",
        "Password!,  false",
        "Password1,  false"
    })
    void csvSource_isValid(String password, boolean expected) {
        // Arrange
        PasswordValidator validator = new PasswordValidator();

        // Act
        boolean result = validator.isValid(password);

        // Assert
        assertEquals(expected, result);
    }

    // ── Test @ValueSource ───────────────────────────────────────────────────

    @ParameterizedTest
    @ValueSource(strings = {"Password1!", "Admin2024@", "Hello123#", "Secure99$", "Test1234%"})
    void valueSource_validPasswords_shouldBeValid(String password) {
        // Arrange
        PasswordValidator validator = new PasswordValidator();

        // Act
        boolean result = validator.isValid(password);

        // Assert
        assertTrue(result);
    }

    // ── Test @MethodSource ──────────────────────────────────────────────────

    static Stream<String> invalidPasswords() {
        return Stream.of("short1!", "PASSWORD1!", "password1!", "Password!", "Password1");
    }

    @ParameterizedTest
    @MethodSource("invalidPasswords")
    void methodSource_invalidPasswords_shouldBeInvalid(String password) {
        // Arrange
        PasswordValidator validator = new PasswordValidator();

        // Act
        boolean result = validator.isValid(password);

        // Assert
        assertFalse(result);
    }

    // ── Vérification des messages d'erreur ──────────────────────────────────

    @Test
    void nullPassword_shouldReturnNullMessage() {
        // Arrange
        PasswordValidator validator = new PasswordValidator();

        // Act
        String message = validator.getErrorMessage(null);

        // Assert
        assertEquals("Password must not be null", message);
    }

    @Test
    void tooShort_shouldReturnLengthMessage() {
        // Arrange
        PasswordValidator validator = new PasswordValidator();

        // Act
        String message = validator.getErrorMessage("short1!");

        // Assert
        assertEquals("Password must contain at least 8 characters", message);
    }

    @Test
    void noLowercase_shouldReturnLowercaseMessage() {
        // Arrange
        PasswordValidator validator = new PasswordValidator();

        // Act
        String message = validator.getErrorMessage("PASSWORD1!");

        // Assert
        assertEquals("Password must contain at least one lowercase letter", message);
    }

    @Test
    void noUppercase_shouldReturnUppercaseMessage() {
        // Arrange
        PasswordValidator validator = new PasswordValidator();

        // Act
        String message = validator.getErrorMessage("password1!");

        // Assert
        assertEquals("Password must contain at least one uppercase letter", message);
    }

    @Test
    void noDigit_shouldReturnDigitMessage() {
        // Arrange
        PasswordValidator validator = new PasswordValidator();

        // Act
        String message = validator.getErrorMessage("Password!");

        // Assert
        assertEquals("Password must contain at least one digit", message);
    }

    @Test
    void noSpecialChar_shouldReturnSpecialCharMessage() {
        // Arrange
        PasswordValidator validator = new PasswordValidator();

        // Act
        String message = validator.getErrorMessage("Password1");

        // Assert
        assertEquals("Password must contain at least one special character", message);
    }

    @Test
    void validPassword_shouldReturnValidMessage() {
        // Arrange
        PasswordValidator validator = new PasswordValidator();

        // Act
        String message = validator.getErrorMessage("Password1!");

        // Assert
        assertEquals("Password is valid", message);
    }

    // ── Bonus : getStrengthScore ────────────────────────────────────────────

    @Test
    void strengthScore_validPassword_shouldReturn5() {
        // Arrange
        PasswordValidator validator = new PasswordValidator();

        // Act
        int score = validator.getStrengthScore("Password1!");

        // Assert
        assertEquals(5, score);
    }

    @Test
    void strengthScore_admin2024_shouldReturn5() {
        // Arrange
        PasswordValidator validator = new PasswordValidator();

        // Act
        int score = validator.getStrengthScore("Admin2024@");

        // Assert
        assertEquals(5, score);
    }

    @Test
    void strengthScore_tooShort_shouldReturn3() {
        // Arrange
        PasswordValidator validator = new PasswordValidator();

        // Act
        int score = validator.getStrengthScore("short1!");

        // Assert
        assertEquals(3, score);
    }

    @Test
    void strengthScore_noLowercase_shouldReturn4() {
        // Arrange
        PasswordValidator validator = new PasswordValidator();

        // Act
        int score = validator.getStrengthScore("PASSWORD1!");

        // Assert
        assertEquals(4, score);
    }

    @Test
    void strengthScore_noUppercase_shouldReturn4() {
        // Arrange
        PasswordValidator validator = new PasswordValidator();

        // Act
        int score = validator.getStrengthScore("password1!");

        // Assert
        assertEquals(4, score);
    }

    @Test
    void strengthScore_noDigit_shouldReturn4() {
        // Arrange
        PasswordValidator validator = new PasswordValidator();

        // Act
        int score = validator.getStrengthScore("Password!");

        // Assert
        assertEquals(4, score);
    }

    @Test
    void strengthScore_noSpecialChar_shouldReturn4() {
        // Arrange
        PasswordValidator validator = new PasswordValidator();

        // Act
        int score = validator.getStrengthScore("Password1");

        // Assert
        assertEquals(4, score);
    }

    @Test
    void strengthScore_null_shouldReturn0() {
        // Arrange
        PasswordValidator validator = new PasswordValidator();

        // Act
        int score = validator.getStrengthScore(null);

        // Assert
        assertEquals(0, score);
    }

    @ParameterizedTest
    @CsvSource({
        "Password1!, 5",
        "Admin2024@, 5",
        "short1!,    3",
        "PASSWORD1!, 4",
        "password1!, 4",
        "Password!,  4",
        "Password1,  4"
    })
    void csvSource_strengthScore(String password, int expected) {
        // Arrange
        PasswordValidator validator = new PasswordValidator();

        // Act
        int score = validator.getStrengthScore(password);

        // Assert
        assertEquals(expected, score);
    }

    // ── Bonus : @NullAndEmptySource ─────────────────────────────────────────

    @ParameterizedTest
    @NullAndEmptySource
    void nullAndEmpty_shouldBeInvalid(String password) {
        // Arrange
        PasswordValidator validator = new PasswordValidator();

        // Act
        boolean result = validator.isValid(password);

        // Assert
        assertFalse(result);
    }
}
