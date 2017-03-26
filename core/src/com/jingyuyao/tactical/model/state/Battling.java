package com.jingyuyao.tactical.model.state;

import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.EventBus;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.model.ModelModule.ModelEventBus;
import com.jingyuyao.tactical.model.battle.Battle;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.item.Target;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.world.Cell;
import javax.inject.Inject;

public class Battling extends AbstractPlayerState {

  private final StateFactory stateFactory;
  private final Battle battle;
  private final Weapon weapon;
  private final Target target;

  @Inject
  Battling(
      @ModelEventBus EventBus eventBus,
      WorldState worldState,
      StateFactory stateFactory,
      Battle battle,
      @Assisted Player player,
      @Assisted Weapon weapon,
      @Assisted Target target) {
    super(eventBus, worldState, stateFactory, player);
    this.stateFactory = stateFactory;
    this.battle = battle;
    this.weapon = weapon;
    this.target = target;
  }

  @Override
  public void select(Cell cell) {
    if (target.canTarget(cell)) {
      attack();
    }
  }

  @Override
  public ImmutableList<Action> getActions() {
    return ImmutableList.of(new AttackAction(this), new BackAction(this));
  }

  public Weapon getWeapon() {
    return weapon;
  }

  public Target getTarget() {
    return target;
  }

  void attack() {
    goTo(stateFactory.createTransition());
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
}
