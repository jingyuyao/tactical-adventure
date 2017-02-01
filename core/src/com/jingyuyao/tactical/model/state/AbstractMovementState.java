package com.jingyuyao.tactical.model.state;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.item.Consumable;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.map.Movements;

abstract class AbstractMovementState extends AbstractPlayerState {

  private final Movements movements;

  AbstractMovementState(
      MapState mapState, StateFactory stateFactory, Movements movements, Player player) {
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

  @Override
  public ImmutableList<Action> getActions() {
    ImmutableList.Builder<Action> builder = new ImmutableList.Builder<Action>();
    // Show the first two weapon and consumable for quick access
    for (Weapon weapon : Iterables.limit(getPlayer().getWeapons(), 1)) {
      builder.add(this.new SelectWeapon(weapon));
    }
    for (Consumable consumable : Iterables.limit(getPlayer().getConsumables(), 1)) {
      builder.add(this.new UseConsumable(consumable));
    }
    if (!getPlayer().fluentItems().isEmpty()) {
      builder.add(this.new SelectItems());
    }
    builder.add(this.new Wait());
    builder.add(this.new Back());
    return builder.build();
  }

  class SelectItems implements Action {

    @Override
    public String getText() {
      return "all items";
    }

    @Override
    public void run() {
      goTo(getStateFactory().createSelectingItem(getPlayer()));
    }
  }

  class Wait implements Action {

    @Override
    public String getText() {
      return "wait";
    }

    @Override
    public void run() {
      finish();
    }
  }
}
