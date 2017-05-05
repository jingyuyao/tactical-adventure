package com.jingyuyao.tactical.model.state;

import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.ModelBus;
import com.jingyuyao.tactical.model.battle.Target;
import com.jingyuyao.tactical.model.item.Consumable;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.world.Cell;
import com.jingyuyao.tactical.model.world.Movements;

class PlayerActionState extends BasePlayerState {

  private final Movements movements;

  PlayerActionState(
      ModelBus modelBus,
      WorldState worldState,
      StateFactory stateFactory,
      Movements movements,
      Cell playerCell) {
    super(modelBus, worldState, stateFactory, playerCell);
    this.movements = movements;
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
    ImmutableList<Target> targets = weapon.createTargets(movements, getPlayerCell());
    goTo(getStateFactory().createSelectingTarget(getPlayerCell(), weapon, targets));
  }

  void selectConsumable(Consumable consumable) {
    goTo(getStateFactory().createUsingConsumable(getPlayerCell(), consumable));
  }

  Movements getMovements() {
    return movements;
  }
}
