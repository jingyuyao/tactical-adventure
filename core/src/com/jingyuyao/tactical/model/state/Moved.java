package com.jingyuyao.tactical.model.state;

import com.google.common.collect.ImmutableList;
import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.map.Movements;
import javax.inject.Inject;

/**
 * Can only perform actions after moving.
 */
class Moved extends AbstractPlayerState {

  private final Movements movements;
  private final ItemActionsFactory itemActionsFactory;

  @Inject
  Moved(
      MapState mapState,
      StateFactory stateFactory,
      Movements movements,
      ItemActionsFactory itemActionsFactory,
      @Assisted Player player) {
    super(mapState, stateFactory, player);
    this.movements = movements;
    this.itemActionsFactory = itemActionsFactory;
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

  @Override
  public ImmutableList<Action> getActions() {
    ImmutableList.Builder<Action> builder = ImmutableList.builder();
    builder.addAll(itemActionsFactory.create(this));
    builder.add(this.new Finish());
    builder.add(this.new Back());
    return builder.build();
  }
}
