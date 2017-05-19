package com.jingyuyao.tactical.model.script;

import com.jingyuyao.tactical.model.state.Turn;
import com.jingyuyao.tactical.model.world.World;

/**
 * Create a {@link Condition} that is met when it reaches {@code target}
 */
public class OnTurn implements Condition {

  private Turn turn;

  OnTurn() {
  }

  OnTurn(Turn turn) {
    this.turn = turn;
  }

  @Override
  public boolean isMet(Turn turn, World world) {
    return this.turn.equals(turn);
  }
}
