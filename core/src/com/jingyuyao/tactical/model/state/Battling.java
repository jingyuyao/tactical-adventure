package com.jingyuyao.tactical.model.state;

import com.google.common.collect.ImmutableList;
import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.model.ModelBus;
import com.jingyuyao.tactical.model.battle.Battle;
import com.jingyuyao.tactical.model.world.Cell;
import javax.inject.Inject;

public class Battling extends BasePlayerState {

  private final StateFactory stateFactory;
  private final BattleSequence battleSequence;
  private final Battle battle;

  @Inject
  Battling(
      ModelBus modelBus,
      WorldState worldState,
      StateFactory stateFactory,
      BattleSequence battleSequence,
      @Assisted Cell playerCell,
      @Assisted Battle battle) {
    super(modelBus, worldState, stateFactory, playerCell);
    this.stateFactory = stateFactory;
    this.battleSequence = battleSequence;
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
    battleSequence.start(battle, new Runnable() {
      @Override
      public void run() {
        finish();
      }
    });
  }
}
