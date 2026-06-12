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
    if (!canRoll()) {
      return false;
    }

    int pins = generateur.randomPin(remainingPins());
    rolls.add(new Roll(pins));
    score += pins;
    return true;
  }

  private boolean canRoll() {
    if (rolls.isEmpty()) {
      return true;
    }

    if (lastFrame) {

      if (rolls.size() == 2) {
        return isStrike() || isSpare();
      }
      return rolls.size() < 3;
    }

    return rolls.size() < 2 && !isStrike();
  }

  private boolean isStrike() {
    return rolls.get(0).getPins() == 10;
  }

  private boolean isSpare() {
    return rolls.size() >= 2
        && !isStrike()
        && rolls.get(0).getPins() + rolls.get(1).getPins() == 10;
  }

  private int remainingPins() {
    if (rolls.isEmpty()) {
      return 10;
    }

    Roll last = rolls.get(rolls.size() - 1);
    if (last.getPins() == 10 || isSpare()) {
      return 10;
    }
    return 10 - last.getPins();
  }

  public int getScore() {
    return score;
  }
}
