package com.jingyuyao.tactical.model.state;

import com.google.common.collect.ImmutableList;
import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.item.Target;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.map.MapObject;
import com.jingyuyao.tactical.model.map.Terrain;
import javax.inject.Inject;

class ReviewingAttack extends AbstractPlayerState {

  private final Weapon weapon;
  private final Target target;

  @Inject
  ReviewingAttack(
      MapState mapState,
      StateFactory stateFactory,
      @Assisted Player player,
      @Assisted Weapon weapon,
      @Assisted Target target) {
    super(mapState, stateFactory, player);
    this.weapon = weapon;
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
    if (target.selectedBy(object.getCoordinate())) {
      attack();
    } else {
      back();
    }
  }

  private void attack() {
    weapon.execute(getPlayer(), target);
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
