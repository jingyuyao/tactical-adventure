package com.jingyuyao.tactical.model.state;

import com.google.common.base.Preconditions;
import com.jingyuyao.tactical.model.ModelBus;
import com.jingyuyao.tactical.model.event.Save;
import com.jingyuyao.tactical.model.ship.Ship;
import com.jingyuyao.tactical.model.state.Turn.TurnStage;
import com.jingyuyao.tactical.model.world.Cell;
import com.jingyuyao.tactical.model.world.World;
import java.util.Collections;
import java.util.List;
import javax.inject.Inject;

/**
 * Note: this is not a {@link TurnScriptState} because it can be entered multiple times when the
 * player cancels their actions. All the scripts for {@link TurnStage#PLAYER} should happen on
 * {@link TurnStage#START} instead.
 */
public class Waiting extends BaseState {

  private final World world;
  private final StateFactory stateFactory;

  @Inject
  Waiting(
      ModelBus modelBus,
      WorldState worldState,
      World world,
      StateFactory stateFactory) {
    super(modelBus, worldState);
    this.world = world;
    this.stateFactory = stateFactory;
  }

  @Override
  public void enter() {
    Preconditions.checkState(getTurn().getStage().equals(TurnStage.PLAYER));
    super.enter();
  }

  @Override
  public void select(Cell cell) {
    for (Ship ship : cell.ship().asSet()) {
      if (ship.isControllable()) {
        goTo(stateFactory.createMoving(cell, world.getShipMovement(cell)));
      }
    }
  }

  @Override
  public List<Action> getActions() {
    return Collections.<Action>singletonList(new EndTurnAction(this));
  }

  void endTurn() {
    advanceTurn();
    post(new Save());
    branchTo(stateFactory.createEndTurn());
  }
}
