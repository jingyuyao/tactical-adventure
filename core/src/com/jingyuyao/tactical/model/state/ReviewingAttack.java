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

class ReviewingAttack extends AbstractPlayerState {

  private final Target target;

  @Inject
  ReviewingAttack(
      EventBus eventBus,
      MapState mapState,
      StateFactory stateFactory,
      @Assisted Player player,
      @Assisted Target target) {
    super(eventBus, mapState, stateFactory, player);
    this.target = target;
  }

  @Override
  public void enter() {
    target.showMarking();
  }

  @Override
  public void exit() {
    target.hideMarking();
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
    return ImmutableList.of(this.new Attack(), this.new Back());
  }

  private void handleSelection(MapObject object) {
    if (target.getSelectCoordinates().contains(object.getCoordinate())) {
      attack();
    } else {
      back();
    }
  }

  private void attack() {
    target.execute();
    finish();
  }

  class Attack implements Action {

    @Override
    public String getName() {
      return "attack";
    }

    @Override
    public void run() {
      attack();
    }
  }
}
