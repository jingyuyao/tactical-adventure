package com.jingyuyao.tactical.model.state;

import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.EventBus;
import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.item.Target;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.map.MapObject;
import com.jingyuyao.tactical.model.map.Terrain;
import javax.inject.Inject;

public class SelectingTarget extends AbstractPlayerState {

  private final Weapon weapon;
  private final ImmutableList<Target> targets;

  @Inject
  SelectingTarget(
      EventBus eventBus,
      MapState mapState,
      StateFactory stateFactory,
      @Assisted Player player,
      @Assisted Weapon weapon,
      @Assisted ImmutableList<Target> targets) {
    super(eventBus, mapState, stateFactory, player);
    this.weapon = weapon;
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
      if (target.selectedBy(object.getCoordinate())) {
        goTo(getStateFactory().createReviewingAttack(getPlayer(), weapon, target));
        return;
      }
    }
    back();
  }
}
