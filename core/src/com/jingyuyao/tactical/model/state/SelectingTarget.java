package com.jingyuyao.tactical.model.state;

import com.google.common.collect.ImmutableList;
import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.model.ModelBus;
import com.jingyuyao.tactical.model.battle.Battle;
import com.jingyuyao.tactical.model.battle.Target;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.world.Cell;
import java.util.List;
import javax.inject.Inject;

public class SelectingTarget extends ControllingState {

  private final Weapon weapon;
  private final List<Target> targets;

  @Inject
  SelectingTarget(
      ModelBus modelBus,
      WorldState worldState,
      StateFactory stateFactory,
      @Assisted Cell cell,
      @Assisted Weapon weapon,
      @Assisted List<Target> targets) {
    super(modelBus, worldState, stateFactory, cell);
    this.weapon = weapon;
    this.targets = targets;
  }

  @Override
  public void select(Cell cell) {
    for (Target target : targets) {
      if (target.selectedBy(cell)) {
        goTo(getStateFactory().createBattling(
            getCell(), new Battle(getCell(), weapon, target)));
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

  public List<Target> getTargets() {
    return targets;
  }
}
