package com.jingyuyao.tactical.model.state;

import com.google.common.base.Preconditions;
import com.jingyuyao.tactical.model.ModelBus;
import com.jingyuyao.tactical.model.event.Save;
import com.jingyuyao.tactical.model.state.Turn.TurnStage;
import com.jingyuyao.tactical.model.world.World;
import javax.inject.Inject;

public class StartTurn extends TurnState {

  private final StateFactory stateFactory;

  @Inject
  StartTurn(
      ModelBus modelBus,
      WorldState worldState,
      World world,
      ScriptRunner scriptRunner,
      StateFactory stateFactory) {
    super(modelBus, worldState, world, scriptRunner);
    this.stateFactory = stateFactory;
  }

  @Override
  public void enter() {
    Preconditions.checkState(getTurn().getStage().equals(TurnStage.START));
    super.enter();
  }

  @Override
  void scriptDone() {
    getTurn().advance();
    post(new Save());
    branchTo(stateFactory.createWaiting());
  }
}
