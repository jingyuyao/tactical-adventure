package com.jingyuyao.tactical.model.state;

import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.ModelBus;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.event.LevelComplete;
import com.jingyuyao.tactical.model.event.LevelFailed;
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
    super.enter();

    boolean levelComplete = true;
    boolean levelFailed = true;
    for (Cell cell : world.getCharacterSnapshot()) {
      if (cell.player().isPresent()) {
        levelFailed = false;
      }

      if (cell.hasEnemy()) {
        levelComplete = false;
      }
    }

    if (levelFailed || levelComplete) {
      for (Cell cell : world.getCharacterSnapshot()) {
        for (Player player : cell.player().asSet()) {
          player.setActionable(true);
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
    for (Player player : cell.player().asSet()) {
      if (player.isActionable()) {
        goTo(stateFactory.createMoving(cell, movements.distanceFrom(cell)));
      }
    }
  }

  @Override
  public ImmutableList<Action> getActions() {
    return ImmutableList.<Action>of(new EndTurnAction(this));
  }

  void endTurn() {
    for (Cell cell : world.getCharacterSnapshot()) {
      for (Player player : cell.player().asSet()) {
        player.setActionable(true);
      }
    }
    goTo(stateFactory.createRetaliating());
  }
}
