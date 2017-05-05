package com.jingyuyao.tactical.model.state;

import com.google.common.collect.ImmutableList;
import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.model.ModelBus;
import com.jingyuyao.tactical.model.battle.Target;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.world.Cell;
import javax.inject.Inject;

public class SelectingTarget extends BasePlayerState {

  private final Weapon weapon;
  private final ImmutableList<Target> targets;

  @Inject
  SelectingTarget(
      ModelBus modelBus,
      WorldState worldState,
      StateFactory stateFactory,
      @Assisted Cell playerCell,
      @Assisted Weapon weapon,
      @Assisted ImmutableList<Target> targets) {
    super(modelBus, worldState, stateFactory, playerCell);
    this.weapon = weapon;
    this.targets = targets;
  }

  @Override
  public void select(Cell cell) {
    for (Target target : targets) {
      if (target.selectedBy(cell)) {
        goTo(getStateFactory().createBattling(getPlayerCell(), weapon, target));
        return;
      }
    }
  }

  @Override
  public ImmutableList<Action> getActions() {
    return ImmutableList.<Action>of(new BackAction(this));
  }

  public Weapon getWeapon() {
    return weapon;
  }

  public ImmutableList<Target> getTargets() {
    return targets;
  }
}
