package com.jingyuyao.tactical.model.state;

import com.google.common.collect.ImmutableList;
import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.model.ModelBus;
import com.jingyuyao.tactical.model.battle.Battle;
import com.jingyuyao.tactical.model.event.MyFuture;
import com.jingyuyao.tactical.model.event.StartBattle;
import com.jingyuyao.tactical.model.world.Cell;
import javax.inject.Inject;

public class Battling extends BasePlayerState {

  private final StateFactory stateFactory;
  private final Battle battle;

  @Inject
  Battling(
      ModelBus modelBus,
      WorldState worldState,
      StateFactory stateFactory,
      @Assisted Cell playerCell,
      @Assisted Battle battle) {
    super(modelBus, worldState, stateFactory, playerCell);
    this.stateFactory = stateFactory;
    this.battle = battle;
  }

  @Override
  public void select(Cell cell) {
    if (battle.getTarget().canTarget(cell)) {
      attack();
    }
  }

  @Override
  public ImmutableList<Action> getActions() {
    return ImmutableList.of(new AttackAction(this), new BackAction(this));
  }

  public Battle getBattle() {
    return battle;
  }

  void attack() {
    goTo(stateFactory.createTransition());
    post(new StartBattle(battle, new MyFuture(new Runnable() {
      @Override
      public void run() {
        finish();
      }
    })));
  }
}
