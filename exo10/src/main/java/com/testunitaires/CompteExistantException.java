package com.testunitaires;

public class CompteExistantException extends RuntimeException {
  public CompteExistantException(String message) {
    super(message);
  }
}
