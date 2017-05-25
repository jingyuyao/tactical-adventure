package com.jingyuyao.tactical.model.state;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * A {@link Turn} has a number and several stages. A turn's number and stage can ONLY go forward.
 * Rollbacks are not allowed so every action have a "permanent" consequence.
 */
public class Turn implements Serializable, Comparable<Turn> {

  private int number;
  private TurnStage stage;

  public Turn() {
    this(1, TurnStage.first());
  }

  public Turn(int number, TurnStage stage) {
    this.number = number;
    this.stage = stage;
  }

  public int getNumber() {
    return number;
  }

  public TurnStage getStage() {
    return stage;
  }

  /**
   * Returns the next {@link Turn}:
   * - if stage is not the last stage, advance it
   * - if stage is the last stage, advance turn number and set stage to the first stage
   */
  Turn advance() {
    if (stage.equals(TurnStage.last())) {
      return new Turn(number + 1, TurnStage.first());
    } else {
      return new Turn(number, TurnStage.next(stage));
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Turn turn = (Turn) o;
    return getNumber() == turn.getNumber() &&
        getStage() == turn.getStage();
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getNumber(), getStage());
  }

  @Override
  public int compareTo(Turn other) {
    int numDiff = number - other.number;
    return numDiff == 0 ? TurnStage.indexOf(stage) - TurnStage.indexOf(other.stage) : numDiff;
  }

  /**
   * Order sensitive. A turn must cycle through every single stage before advancing.
   */
  public enum TurnStage {
    /**
     * Scripting supported.
     */
    START,
    /**
     * Scripting NOT supported. Use {@link #START} for scripting instead.
     */
    PLAYER,
    /**
     * Scripting supported.
     */
    END,
    /**
     * Scripting NOT supported. Use {@link #END} for scripting instead.
     */
    ENEMY;

    private static TurnStage first() {
      return values()[0];
    }

    private static TurnStage last() {
      return values()[values().length - 1];
    }

    private static TurnStage next(TurnStage stage) {
      List<TurnStage> stages = Arrays.asList(values());
      int index = stages.indexOf(stage);
      Preconditions.checkArgument(index != -1);
      return stages.get(index + 1);
    }

    private static int indexOf(TurnStage stage) {
      return Arrays.asList(values()).indexOf(stage);
    }
  }
}
