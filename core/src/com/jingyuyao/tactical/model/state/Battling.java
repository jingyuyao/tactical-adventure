package com.jingyuyao.tactical.model.state;

import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.EventBus;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.model.ModelModule.ModelEventBus;
import com.jingyuyao.tactical.model.battle.Battle;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.event.HideTarget;
import com.jingyuyao.tactical.model.event.ShowTarget;
import com.jingyuyao.tactical.model.item.Target;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.map.MapObject;
import com.jingyuyao.tactical.model.terrain.Terrain;
import javax.inject.Inject;

class Battling extends BasePlayerState {

  private final Battle battle;
  private final Weapon weapon;
  private final Target target;

  @Inject
  Battling(
      MapState mapState,
      StateFactory stateFactory,
      @ModelEventBus EventBus eventBus,
      Battle battle,
      @Assisted Player player,
      @Assisted Weapon weapon,
      @Assisted Target target) {
    super(mapState, stateFactory, eventBus, player);
    this.battle = battle;
    this.weapon = weapon;
    this.target = target;
  }

  @Override
  public void enter() {
    super.enter();
    getEventBus().post(new ShowTarget(target));
  }

  @Override
  public void exit() {
    super.exit();
    getEventBus().post(new HideTarget(target));
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
    return ImmutableList.of(new AttackAction(this), new BackAction(this));
  }

  void attack() {
    goTo(getStateFactory().createTransition());
    Futures.addCallback(battle.begin(getPlayer(), weapon, target), new FutureCallback<Void>() {
      @Override
      public void onSuccess(Void result) {
        finish();
      }

      @Override
      public void onFailure(Throwable t) {

      }
    });
  }

  private void handleSelection(MapObject object) {
    if (target.canTarget(object.getCoordinate())) {
      attack();
    }
  }
}