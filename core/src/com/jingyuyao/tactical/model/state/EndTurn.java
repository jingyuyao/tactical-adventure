package com.jingyuyao.tactical.model.state;

import com.google.common.base.Preconditions;
import com.jingyuyao.tactical.model.ModelBus;
import com.jingyuyao.tactical.model.event.Save;
import com.jingyuyao.tactical.model.ship.Player;
import com.jingyuyao.tactical.model.state.Turn.TurnStage;
import com.jingyuyao.tactical.model.world.Cell;
import com.jingyuyao.tactical.model.world.World;
import javax.inject.Inject;

public class EndTurn extends ScriptState {

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
    Preconditions.checkState(getTurn().getStage().equals(TurnStage.END));
    super.enter();
  }

  @Override
  void finish() {
    makePlayersActionable();
    getTurn().advance();
    post(new Save());
    branchTo(stateFactory.createRetaliating());
  }

  private void makePlayersActionable() {
    for (Cell cell : world.getShipSnapshot()) {
      for (Player player : cell.player().asSet()) {
        player.setActionable(true);
      }
    }
  }
}
