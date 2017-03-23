package com.jingyuyao.tactical.model.state;

import com.google.common.eventbus.EventBus;
import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.model.ModelModule.ModelEventBus;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.map.Cell;
import com.jingyuyao.tactical.model.map.Movements;
import javax.inject.Inject;

/**
 * Can only perform actions after moving.
 */
public class Moved extends BasePlayerState {

  private final StateFactory stateFactory;
  private final Movements movements;

  @Inject
  Moved(
      @ModelEventBus EventBus eventBus,
      MapState mapState,
      StateFactory stateFactory,
      Movements movements,
      @Assisted Player player) {
    super(eventBus, mapState, stateFactory, player);
    this.stateFactory = stateFactory;
    this.movements = movements;
  }

  @Override
  public void select(Cell cell) {
    if (cell.hasPlayer()) {
      Player player = cell.getPlayer();
      if (!getPlayer().equals(player)) {
        rollback();
        if (player.isActionable()) {
          goTo(stateFactory.createMoving(player, movements.distanceFrom(cell)));
        }
      }
    }
  }
}
