package com.jingyuyao.tactical.model.script;

import com.jingyuyao.tactical.model.state.Turn;
import com.jingyuyao.tactical.model.world.World;

public class NewTurn implements ScriptEvent {

  private final Turn turn;
  private final World world;

  public NewTurn(Turn turn, World world) {
    this.turn = turn;
    this.world = world;
  }

  @Override
  public boolean satisfiedBy(Condition condition) {
    return condition.onTurn(turn, world);
  }

  public Turn getTurn() {
    return turn;
  }

  public World getWorld() {
    return world;
  }
}
