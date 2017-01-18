package com.jingyuyao.tactical.model.state;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.item.Consumable;
import com.jingyuyao.tactical.model.item.Weapon;

abstract class AbstractMovementState extends AbstractPlayerState {

  AbstractMovementState(
      EventBus eventBus,
      MapState mapState,
      StateFactory stateFactory,
      Player player) {
    super(eventBus, mapState, stateFactory, player);
  }

  @Override
  public void select(Player player) {
    if (getPlayer().equals(player)) {
      back();
    } else {
      rollback();
      goTo(getStateFactory().createMoving(player));
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
    if (!Iterables.isEmpty(getPlayer().getItems())) {
      builder.add(this.new SelectItems());
    }
    builder.add(this.new Wait());
    builder.add(this.new Back());
    return builder.build();
  }
}
