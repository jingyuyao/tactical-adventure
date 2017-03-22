package com.jingyuyao.tactical.model.state;

import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.EventBus;
import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.model.ModelModule.ModelEventBus;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.item.Target;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.map.Cell;
import javax.inject.Inject;

public class SelectingTarget extends BasePlayerState {

  private final StateFactory stateFactory;
  private final Weapon weapon;
  private final ImmutableList<Target> targets;

  @Inject
  SelectingTarget(
      @ModelEventBus EventBus eventBus,
      MapState mapState,
      StateFactory stateFactory,
      @Assisted Player player,
      @Assisted Weapon weapon,
      @Assisted ImmutableList<Target> targets) {
    super(eventBus, mapState, stateFactory, player);
    this.stateFactory = stateFactory;
    this.weapon = weapon;
    this.targets = targets;
  }

  @Override
  public void select(Cell cell) {
    for (Target target : targets) {
      if (target.selectedBy(cell.getCoordinate())) {
        goTo(stateFactory.createBattling(getPlayer(), weapon, target));
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
