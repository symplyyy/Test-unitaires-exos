package com.testunitaire;

public class PasswordValidator {

    public boolean isValid(String password) {
        return getErrorMessage(password).equals("Password is valid");
    }

    public String getErrorMessage(String password) {
        if (password == null)
            return "Password must not be null";
        if (password.length() < 8)
            return "Password must contain at least 8 characters";
        if (password.chars().noneMatch(Character::isLowerCase))
            return "Password must contain at least one lowercase letter";
        if (password.chars().noneMatch(Character::isUpperCase))
            return "Password must contain at least one uppercase letter";
        if (password.chars().noneMatch(Character::isDigit))
            return "Password must contain at least one digit";
        if (password.chars().noneMatch(c -> "!@#$%".indexOf(c) >= 0))
            return "Password must contain at least one special character";
        return "Password is valid";
    }
}
