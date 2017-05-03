package com.jingyuyao.tactical.model.state;

import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.ModelBus;
import com.jingyuyao.tactical.model.item.Consumable;
import com.jingyuyao.tactical.model.item.Target;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.world.Cell;

class PlayerActionState extends AbstractPlayerState {

  private final Cell cell;

  PlayerActionState(
      ModelBus modelBus, WorldState worldState, StateFactory stateFactory, Cell cell) {
    super(modelBus, worldState, stateFactory, cell.player().orNull());
    this.cell = cell;
  }

  @Override
  public ImmutableList<Action> getActions() {
    ImmutableList.Builder<Action> builder = ImmutableList.builder();
    for (Weapon weapon : getPlayer().getWeapons()) {
      builder.add(new SelectWeaponAction(this, weapon));
    }
    for (Consumable consumable : getPlayer().getConsumables()) {
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
