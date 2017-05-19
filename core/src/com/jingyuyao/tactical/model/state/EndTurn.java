package com.jingyuyao.tactical.model.state;

import com.google.common.base.Preconditions;
import com.jingyuyao.tactical.model.ModelBus;
import com.jingyuyao.tactical.model.event.Save;
import com.jingyuyao.tactical.model.state.Turn.TurnStage;
import com.jingyuyao.tactical.model.world.World;
import javax.inject.Inject;

public class EndTurn extends TurnScriptState {

  private final StateFactory stateFactory;
  private final World world;

  @Inject
  EndTurn(
      ModelBus modelBus,
      WorldState worldState,
      LevelComplete levelComplete,
      StateFactory stateFactory,
      World world) {
    super(modelBus, worldState, levelComplete);
    this.stateFactory = stateFactory;
    this.world = world;
  }

  @Override
  public void enter() {
    Preconditions.checkState(getTurn().getStage().equals(TurnStage.END));
    super.enter();
  }

  @Override
  void scriptDone() {
    world.makeAllPlayerShipsControllable();
    getTurn().advance();
    post(new Save());
    branchTo(stateFactory.createRetaliating());
  }
}
