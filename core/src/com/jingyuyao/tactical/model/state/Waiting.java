package com.jingyuyao.tactical.model.state;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.ModelBus;
import com.jingyuyao.tactical.model.event.Save;
import com.jingyuyao.tactical.model.ship.Ship;
import com.jingyuyao.tactical.model.state.Turn.TurnStage;
import com.jingyuyao.tactical.model.world.Cell;
import com.jingyuyao.tactical.model.world.World;
import javax.inject.Inject;

public class Waiting extends TurnState {

  private final StateFactory stateFactory;

  @Inject
  Waiting(
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
    Preconditions.checkState(getTurn().getStage().equals(TurnStage.PLAYER));
    super.enter();
  }

  @Override
  void scriptDone() {
  }

  @Override
  public void select(Cell cell) {
    for (Ship ship : cell.ship().asSet()) {
      if (ship.isControllable()) {
        goTo(stateFactory.createMoving(cell, getWorld().getShipMovement(cell)));
      }
    }
  }

  @Override
  public ImmutableList<Action> getActions() {
    return ImmutableList.<Action>of(new EndTurnAction(this));
  }

  void endTurn() {
    advanceTurn();
    post(new Save());
    branchTo(stateFactory.createEndTurn());
  }
}
