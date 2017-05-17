package com.jingyuyao.tactical.model.state;

import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.ModelBus;
import com.jingyuyao.tactical.model.battle.Target;
import com.jingyuyao.tactical.model.item.Consumable;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.world.Cell;
import com.jingyuyao.tactical.model.world.Movements;

class ControllingActionState extends ControllingState {

  private final Movements movements;

  ControllingActionState(
      ModelBus modelBus,
      WorldState worldState,
      StateFactory stateFactory,
      Movements movements,
      Cell cell) {
    super(modelBus, worldState, stateFactory, cell);
    this.movements = movements;
  }

  @Override
  public ImmutableList<Action> getActions() {
    ImmutableList.Builder<Action> builder = ImmutableList.builder();
    for (Weapon weapon : getShip().getWeapons()) {
      builder.add(new SelectWeaponAction(this, weapon));
    }
    for (Consumable consumable : getShip().getConsumables()) {
      builder.add(new SelectConsumableAction(this, consumable));
    }
    builder.add(new FinishAction(this));
    builder.add(new BackAction(this));
    return builder.build();
  }

  void selectWeapon(Weapon weapon) {
    ImmutableList<Target> targets = weapon.createTargets(movements, getCell());
    goTo(getStateFactory().createSelectingTarget(getCell(), weapon, targets));
  }

  void selectConsumable(Consumable consumable) {
    goTo(getStateFactory().createUsingConsumable(getCell(), consumable));
  }

  Movements getMovements() {
    return movements;
  }
}
