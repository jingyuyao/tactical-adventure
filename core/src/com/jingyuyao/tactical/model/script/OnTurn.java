package com.jingyuyao.tactical.model.script;

import com.jingyuyao.tactical.model.state.Turn;
import com.jingyuyao.tactical.model.world.World;

/**
 * Met when the current turn reaches the set turn.
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
