package com.jingyuyao.tactical.model.state;

import com.google.common.collect.ImmutableList;
import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.model.ModelBus;
import com.jingyuyao.tactical.model.battle.Battle;
import com.jingyuyao.tactical.model.battle.Target;
import com.jingyuyao.tactical.model.event.MyFuture;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.world.Cell;
import javax.inject.Inject;

public class Battling extends BasePlayerState {

  private final StateFactory stateFactory;
  private final Weapon weapon;
  private final Target target;

  @Inject
  Battling(
      ModelBus modelBus,
      WorldState worldState,
      StateFactory stateFactory,
      @Assisted Cell playerCell,
      @Assisted Weapon weapon,
      @Assisted Target target) {
    super(modelBus, worldState, stateFactory, playerCell);
    this.stateFactory = stateFactory;
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
    post(new Battle(getPlayerCell(), weapon, target, new MyFuture(new Runnable() {
      @Override
      public void run() {
        finish();
      }
    })));
  }
}
