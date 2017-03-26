package com.jingyuyao.tactical.model.state;

import com.google.common.eventbus.EventBus;
import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.model.ModelModule.ModelEventBus;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.world.Cell;
import com.jingyuyao.tactical.model.world.Movements;
import javax.inject.Inject;

/**
 * Can only perform actions after moving.
 */
public class Moved extends PlayerActionState {

  private final Movements movements;

  @Inject
  Moved(
      @ModelEventBus EventBus eventBus,
      WorldState worldState,
      StateFactory stateFactory,
      Movements movements,
      @Assisted Cell cell) {
    super(eventBus, worldState, stateFactory, cell);
    this.movements = movements;
  }

  @Override
  public void select(Cell cell) {
    if (cell.hasPlayer()) {
      Player player = cell.getPlayer();
      if (!getPlayer().equals(player)) {
        rollback();
        if (player.isActionable()) {
          goTo(getStateFactory().createMoving(cell, movements.distanceFrom(cell)));
        }
      }
    }
  }
}
