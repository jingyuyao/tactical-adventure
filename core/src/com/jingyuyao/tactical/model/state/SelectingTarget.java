package com.jingyuyao.tactical.model.state;

import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.EventBus;
import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.model.battle.Target;
import com.jingyuyao.tactical.model.item.Weapon;
import javax.inject.Inject;

// TODO: stub
public class SelectingTarget extends AbstractState {

  private final Weapon weapon;
  private final ImmutableList<Target> targets;

  @Inject
  SelectingTarget(
      EventBus eventBus,
      MapState mapState,
      StateFactory stateFactory,
      @Assisted Weapon weapon,
      @Assisted ImmutableList<Target> targets) {
    super(eventBus, mapState, stateFactory);
    this.weapon = weapon;
    this.targets = targets;
  }

  @Override
  public ImmutableList<Action> getActions() {
    return ImmutableList.<Action>of(this.new Back());
  }
}
