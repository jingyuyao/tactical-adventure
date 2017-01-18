package com.jingyuyao.tactical.model.state;

import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.EventBus;
import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.map.MapObject;
import com.jingyuyao.tactical.model.map.Terrain;
import com.jingyuyao.tactical.model.target.Target;
import javax.inject.Inject;

public class SelectingTarget extends AbstractPlayerState {

  private final ImmutableList<Target> targets;

  @Inject
  SelectingTarget(
      EventBus eventBus,
      MapState mapState,
      StateFactory stateFactory,
      @Assisted Player player,
      @Assisted ImmutableList<Target> targets) {
    super(eventBus, mapState, stateFactory, player);
    this.targets = targets;
  }

  @Override
  public void enter() {
    for (Target target : targets) {
      target.showMarking();
    }
  }

  @Override
  public void exit() {
    for (Target target : targets) {
      target.hideMarking();
    }
  }

  @Override
  public void select(Player player) {
    handleSelection(player);
  }

  @Override
  public void select(Enemy enemy) {
    handleSelection(enemy);
  }

  @Override
  public void select(Terrain terrain) {
    handleSelection(terrain);
  }

  @Override
  public ImmutableList<Action> getActions() {
    return ImmutableList.<Action>of(this.new Back());
  }

  private void handleSelection(MapObject object) {
    for (Target target : targets) {
      if (target.getSelectObjects().contains(object)) {
        goTo(getStateFactory().createReviewingAttack(getPlayer(), target));
        return;
      }
    }
    back();
  }
}
