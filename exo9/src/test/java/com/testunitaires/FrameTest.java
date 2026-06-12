package com.testunitaires;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FrameTest {

  @Mock
  private IGenerateur generateur;

  private Frame frameWithRolls(boolean lastFrame, int first, int... others) {
    when(generateur.randomPin(anyInt())).thenReturn(first, box(others));
    return new Frame(generateur, lastFrame);
  }

  private Integer[] box(int[] values) {
    Integer[] boxed = new Integer[values.length];
    for (int i = 0; i < values.length; i++) {
      boxed[i] = values[i];
    }
    return boxed;
  }

  @Test
  void shouldIncreaseScoreWhenFirstRollIsMadeInStandardFrame() {
    Frame frame = frameWithRolls(false, 4);

    frame.makeRoll();

    assertEquals(4, frame.getScore());
  }

  @Test
  void shouldIncreaseScoreWhenSecondRollIsMadeInStandardFrame() {
    Frame frame = frameWithRolls(false, 4, 3);

    frame.makeRoll();
    frame.makeRoll();

    assertEquals(7, frame.getScore());
  }

  @Test
  void shouldRejectSecondRollWhenStandardFrameStartsWithStrike() {
    Frame frame = frameWithRolls(false, 10);

    frame.makeRoll();

    assertFalse(frame.makeRoll());
  }

  @Test
  void shouldRejectThirdRollWhenStandardFrameAlreadyHasTwoRolls() {
    Frame frame = frameWithRolls(false, 4, 3);

    frame.makeRoll();
    frame.makeRoll();

    assertFalse(frame.makeRoll());
  }

  @Test
  void shouldIncreaseScoreWhenSecondRollIsMadeAfterStrikeInLastFrame() {
    Frame frame = frameWithRolls(true, 10, 5);

    frame.makeRoll();
    frame.makeRoll();

    assertEquals(15, frame.getScore());
  }

  @Test
  void shouldAcceptSecondRollWhenLastFrameStartsWithStrike() {
    Frame frame = frameWithRolls(true, 10);

    frame.makeRoll();

    assertTrue(frame.makeRoll());
  }

  @Test
  void shouldAcceptThirdRollWhenLastFrameStartsWithStrike() {
    Frame frame = frameWithRolls(true, 10, 5);

    frame.makeRoll();
    frame.makeRoll();

    assertTrue(frame.makeRoll());
  }

  @Test
  void shouldIncreaseScoreWhenThirdRollIsMadeAfterStrikeInLastFrame() {
    Frame frame = frameWithRolls(true, 10, 5, 3);

    frame.makeRoll();
    frame.makeRoll();
    frame.makeRoll();

    assertEquals(18, frame.getScore());
  }

  @Test
  void shouldAcceptThirdRollWhenLastFrameStartsWithSpare() {
    Frame frame = frameWithRolls(true, 4, 6);

    frame.makeRoll();
    frame.makeRoll();

    assertTrue(frame.makeRoll());
  }

  @Test
  void shouldIncreaseScoreWhenThirdRollIsMadeAfterSpareInLastFrame() {
    Frame frame = frameWithRolls(true, 4, 6, 5);

    frame.makeRoll();
    frame.makeRoll();
    frame.makeRoll();

    assertEquals(15, frame.getScore());
  }

  @Test
  void shouldRejectThirdRollWhenLastFrameHasNoStrikeOrSpare() {
    Frame frame = frameWithRolls(true, 4, 3);

    frame.makeRoll();
    frame.makeRoll();

    assertFalse(frame.makeRoll());
  }

  @Test
  void shouldRejectFourthRollInLastFrame() {
    Frame frame = frameWithRolls(true, 10, 5, 3);

    frame.makeRoll();
    frame.makeRoll();
    frame.makeRoll();

    assertFalse(frame.makeRoll());
  }
}
