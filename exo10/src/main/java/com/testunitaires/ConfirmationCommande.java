package com.testunitaires;

public class ConfirmationCommande {
  private final String commandeId;
  private final String message;

  public ConfirmationCommande(String commandeId, String message) {
    this.commandeId = commandeId;
    this.message = message;
  }

  public String getCommandeId() {
    return commandeId;
  }

  public String getMessage() {
    return message;
  }
}
