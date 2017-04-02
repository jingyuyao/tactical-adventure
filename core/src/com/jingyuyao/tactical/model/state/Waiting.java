package com.jingyuyao.tactical.model.state;

import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.model.ModelModule.ModelEventBus;
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
      @ModelEventBus EventBus eventBus,
      WorldState worldState,
      StateFactory stateFactory,
      World world,
      Movements movements) {
    super(eventBus, worldState);
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
      if (cell.hasPlayer()) {
        levelFailed = false;
      } else if (cell.hasEnemy()) {
        levelComplete = false;
      }
    }

    if (levelFailed || levelComplete) {
      for (Cell cell : world.getCharacterSnapshot()) {
        if (cell.hasPlayer()) {
          cell.getPlayer().setActionable(true);
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
    if (cell.hasPlayer()) {
      Player player = cell.getPlayer();
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
      if (cell.hasPlayer()) {
        cell.getPlayer().setActionable(true);
      }
    }
    goTo(stateFactory.createRetaliating());
  }
}
