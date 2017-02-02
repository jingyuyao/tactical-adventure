package com.jingyuyao.tactical.model.state;

import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.map.Movements;
import javax.inject.Inject;

/**
 * Can only perform actions after moving.
 */
class Moved extends ItemSelectionState {

  private final Movements movements;

  @Inject
  Moved(
      MapState mapState, StateFactory stateFactory, Movements movements, @Assisted Player player) {
    super(mapState, stateFactory, player);
    this.movements = movements;
  }

  @Override
  public void select(Player player) {
    if (getPlayer().equals(player)) {
      back();
    } else {
      rollback();
      if (player.isActionable()) {
        goTo(getStateFactory().createMoving(player, movements.distanceFrom(player)));
      }
    }
  }
}
