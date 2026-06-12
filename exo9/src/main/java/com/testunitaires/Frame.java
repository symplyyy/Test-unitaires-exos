package com.testunitaires;

import java.util.ArrayList;
import java.util.List;

public class Frame {
  private int score;
  private final boolean lastFrame;
  private final IGenerateur generateur;
  private final List<Roll> rolls = new ArrayList<>();

  public Frame(IGenerateur generateur, boolean lastFrame) {
    this.lastFrame = lastFrame;
    this.generateur = generateur;
  }

  public boolean makeRoll() {
    throw new UnsupportedOperationException("not implemented");
  }

  public int getScore() {
    throw new UnsupportedOperationException("not implemented");
  }
}
