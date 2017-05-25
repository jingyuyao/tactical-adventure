package com.jingyuyao.tactical.model.state;

import com.jingyuyao.tactical.model.ModelBus;
import com.jingyuyao.tactical.model.battle.Target;
import com.jingyuyao.tactical.model.item.Consumable;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.world.Cell;
import com.jingyuyao.tactical.model.world.World;
import java.util.ArrayList;
import java.util.List;

class ControllingActionState extends ControllingState {

  private final World world;

  ControllingActionState(
      ModelBus modelBus,
      WorldState worldState,
      StateFactory stateFactory,
      World world,
      Cell cell) {
    super(modelBus, worldState, stateFactory, cell);
    this.world = world;
  }

  @Override
  public List<Action> getActions() {
    List<Action> actions = new ArrayList<>();
    for (Weapon weapon : getShip().getWeapons()) {
      actions.add(new SelectWeaponAction(this, weapon));
    }
    for (Consumable consumable : getShip().getConsumables()) {
      actions.add(new SelectConsumableAction(this, consumable));
    }
    actions.add(new FinishAction(this));
    actions.add(new BackAction(this));
    return actions;
  }

  void selectWeapon(Weapon weapon) {
    List<Target> targets = weapon.createTargets(world, getCell());
    goTo(getStateFactory().createSelectingTarget(getCell(), weapon, targets));
  }

  void selectConsumable(Consumable consumable) {
    goTo(getStateFactory().createUsingConsumable(getCell(), consumable));
  }

  World getWorld() {
    return world;
  }
}
