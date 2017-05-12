package com.jingyuyao.tactical.model.state;

import com.jingyuyao.tactical.model.ModelBus;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.world.Cell;
import com.jingyuyao.tactical.model.world.World;
import javax.inject.Inject;

public class EndTurn extends BaseState {

  private final StateFactory stateFactory;
  private final World world;

  @Inject
  EndTurn(ModelBus modelBus, WorldState worldState, StateFactory stateFactory, World world) {
    super(modelBus, worldState);
    this.stateFactory = stateFactory;
    this.world = world;
  }

  @Override
  public void enter() {
    super.enter();
    makePlayersActionable();
    // TODO: check script for dialogue before finishing
    finish();
  }

  private void makePlayersActionable() {
    for (Cell cell : world.getCharacterSnapshot()) {
      for (Player player : cell.player().asSet()) {
        player.setActionable(true);
      }
    }
  }

  private void finish() {
    incrementTurn();
    branchTo(stateFactory.createRetaliating());
  }
}
