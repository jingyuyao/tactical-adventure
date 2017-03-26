package com.jingyuyao.tactical.model.state;

import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.EventBus;
import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.model.ModelModule.ModelEventBus;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.item.Target;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.world.Cell;
import javax.inject.Inject;

public class SelectingTarget extends AbstractPlayerState {

  private final Weapon weapon;
  private final ImmutableList<Target> targets;

  @Inject
  SelectingTarget(
      @ModelEventBus EventBus eventBus,
      WorldState worldState,
      StateFactory stateFactory,
      @Assisted Player player,
      @Assisted Weapon weapon,
      @Assisted ImmutableList<Target> targets) {
    super(eventBus, worldState, stateFactory, player);
    this.weapon = weapon;
    this.targets = targets;
  }

  @Override
  public void select(Cell cell) {
    for (Target target : targets) {
      if (target.selectedBy(cell)) {
        goTo(getStateFactory().createBattling(getPlayer(), weapon, target));
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
