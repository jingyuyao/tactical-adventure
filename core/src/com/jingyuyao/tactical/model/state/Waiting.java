package com.jingyuyao.tactical.model.state;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.Allegiance;
import com.jingyuyao.tactical.model.ModelBus;
import com.jingyuyao.tactical.model.event.LevelComplete;
import com.jingyuyao.tactical.model.event.LevelFailed;
import com.jingyuyao.tactical.model.event.Save;
import com.jingyuyao.tactical.model.ship.Ship;
import com.jingyuyao.tactical.model.state.Turn.TurnStage;
import com.jingyuyao.tactical.model.world.Cell;
import com.jingyuyao.tactical.model.world.Movements;
import com.jingyuyao.tactical.model.world.World;
import javax.inject.Inject;

public class Waiting extends BaseState {

  private final StateFactory stateFactory;
  private final World world;
  private final Movements movements;

  @Inject
  Waiting(
      ModelBus modelBus,
      WorldState worldState,
      StateFactory stateFactory,
      World world,
      Movements movements) {
    super(modelBus, worldState);
    this.stateFactory = stateFactory;
    this.world = world;
    this.movements = movements;
  }

  @Override
  public void enter() {
    Preconditions.checkState(getTurn().getStage().equals(TurnStage.PLAYER));
    super.enter();

    boolean levelComplete = true;
    boolean levelFailed = true;
    for (Cell cell : world.getShipSnapshot()) {
      for (Ship ship : cell.ship().asSet()) {
        switch (ship.getAllegiance()) {
          case PLAYER:
            levelFailed = false;
            break;
          case ENEMY:
            levelComplete = false;
            break;
        }
      }
    }

    if (levelFailed || levelComplete) {
      for (Cell cell : world.getShipSnapshot()) {
        for (Ship ship : cell.ship().asSet()) {
          if (ship.getAllegiance().equals(Allegiance.PLAYER)) {
            ship.setControllable(true);
          }
        }
      }
    }

    if (levelFailed) {
      post(new LevelFailed());
    } else if (levelComplete) {
      post(new LevelComplete());
    }
  }

  @Override
  public void select(Cell cell) {
    for (Ship ship : cell.ship().asSet()) {
      if (ship.isControllable()) {
        goTo(stateFactory.createMoving(cell, movements.distanceFrom(cell)));
      }
    }
  }

  @Override
  public ImmutableList<Action> getActions() {
    return ImmutableList.<Action>of(new EndTurnAction(this));
  }

  void endTurn() {
    getTurn().advance();
    post(new Save());
    branchTo(stateFactory.createEndTurn());
  }
}
