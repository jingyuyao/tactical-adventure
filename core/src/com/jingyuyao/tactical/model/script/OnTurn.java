package com.jingyuyao.tactical.model.script;

import com.google.common.base.Objects;
import com.jingyuyao.tactical.model.state.Turn;
import com.jingyuyao.tactical.model.world.World;

/**
 * Satisfies when the current turn reaches the set turn.
 */
public class OnTurn extends Condition {

  private final Turn turn;

  public OnTurn(Turn turn) {
    this.turn = turn;
  }

  @Override
  public boolean onTurn(Turn turn, World world) {
    return this.turn.equals(turn);
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (object == null || getClass() != object.getClass()) {
      return false;
    }
    OnTurn onTurn = (OnTurn) object;
    return Objects.equal(turn, onTurn.turn);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(turn);
  }
}
