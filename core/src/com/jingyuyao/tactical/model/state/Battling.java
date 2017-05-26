package com.jingyuyao.tactical.model.state;

import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.model.ModelBus;
import com.jingyuyao.tactical.model.battle.Battle;
import com.jingyuyao.tactical.model.world.Cell;
import java.util.Arrays;
import java.util.List;
import javax.inject.Inject;

public class Battling extends ControllingState {

  private final StateFactory stateFactory;
  private final BattleSequence battleSequence;
  private final Battle battle;

  @Inject
  Battling(
      ModelBus modelBus,
      WorldState worldState,
      StateFactory stateFactory,
      BattleSequence battleSequence,
      @Assisted Cell cell,
      @Assisted Battle battle) {
    super(modelBus, worldState, stateFactory, cell);
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
  public List<Action> getActions() {
    return Arrays.asList(new AttackAction(this), new BackAction(this));
  }

  public Battle getBattle() {
    return battle;
  }

  void attack() {
    branchTo(stateFactory.createTransition());
    battleSequence.start(battle, new Runnable() {
      @Override
      public void run() {
        finish();
      }
    });
  }
}
