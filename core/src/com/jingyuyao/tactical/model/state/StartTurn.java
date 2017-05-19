package com.jingyuyao.tactical.model.state;

import com.google.common.base.Preconditions;
import com.jingyuyao.tactical.model.ModelBus;
import com.jingyuyao.tactical.model.event.Save;
import com.jingyuyao.tactical.model.state.Turn.TurnStage;
import javax.inject.Inject;

public class StartTurn extends ScriptState {

  private final StateFactory stateFactory;

  @Inject
  StartTurn(
      ModelBus modelBus,
      WorldState worldState,
      LevelComplete levelComplete,
      StateFactory stateFactory) {
    super(modelBus, worldState, levelComplete);
    this.stateFactory = stateFactory;
  }

  @Override
  public void enter() {
    Preconditions.checkState(getTurn().getStage().equals(TurnStage.START));
    super.enter();
  }

  @Override
  void finish() {
    getTurn().advance();
    post(new Save());
    branchTo(stateFactory.createWaiting());
  }
}
