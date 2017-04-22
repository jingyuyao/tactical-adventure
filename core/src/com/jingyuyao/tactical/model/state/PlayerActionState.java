package com.jingyuyao.tactical.model.state;

import com.google.common.base.Preconditions;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.model.item.Consumable;
import com.jingyuyao.tactical.model.item.Item;
import com.jingyuyao.tactical.model.item.Target;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.world.Cell;

class PlayerActionState extends AbstractPlayerState {

  private final Cell cell;

  PlayerActionState(
      EventBus eventBus,
      WorldState worldState,
      StateFactory stateFactory,
      Cell cell) {
    super(eventBus, worldState, stateFactory, cell.getPlayer());
    Preconditions.checkArgument(cell.hasPlayer());
    this.cell = cell;
  }

  @Override
  public ImmutableList<Action> getActions() {
    ImmutableList.Builder<Action> builder = ImmutableList.builder();
    FluentIterable<Item> items = getPlayer().fluentItems();
    for (Weapon weapon : items.filter(Weapon.class)) {
      builder.add(new SelectWeaponAction(this, weapon));
    }
    for (Consumable consumable : items.filter(Consumable.class)) {
      builder.add(new SelectConsumableAction(this, consumable));
    }
    builder.add(new FinishAction(this));
    builder.add(new BackAction(this));
    return builder.build();
  }

  void selectWeapon(Weapon weapon) {
    ImmutableList<Target> targets = weapon.createTargets(cell);
    goTo(getStateFactory().createSelectingTarget(getPlayer(), weapon, targets));
  }

  void selectConsumable(Consumable consumable) {
    goTo(getStateFactory().createUsingConsumable(getPlayer(), consumable));
  }
}
