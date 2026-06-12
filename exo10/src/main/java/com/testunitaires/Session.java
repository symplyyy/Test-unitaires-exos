package com.testunitaires;

public class Session {
  private final String username;
  private final String page;

  public Session(String username, String page) {
    this.username = username;
    this.page = page;
  }

  public String getUsername() {
    return username;
  }

  public String getPage() {
    return page;
  }
}
