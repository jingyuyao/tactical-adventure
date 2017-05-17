package com.jingyuyao.tactical.model.state;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.ModelBus;
import com.jingyuyao.tactical.model.event.ActivatedEnemy;
import com.jingyuyao.tactical.model.event.Save;
import com.jingyuyao.tactical.model.ship.AutoPilot;
import com.jingyuyao.tactical.model.ship.Enemy;
import com.jingyuyao.tactical.model.state.Turn.TurnStage;
import com.jingyuyao.tactical.model.world.Cell;
import com.jingyuyao.tactical.model.world.Movements;
import com.jingyuyao.tactical.model.world.Path;
import com.jingyuyao.tactical.model.world.World;
import javax.inject.Inject;

public class Retaliating extends BaseState {

  private final StateFactory stateFactory;
  private final Movements movements;
  private final World world;
  private final BattleSequence battleSequence;

  @Inject
  Retaliating(
      ModelBus modelBus,
      WorldState worldState,
      StateFactory stateFactory,
      Movements movements,
      World world,
      BattleSequence battleSequence) {
    super(modelBus, worldState);
    this.stateFactory = stateFactory;
    this.movements = movements;
    this.world = world;
    this.battleSequence = battleSequence;
  }

  @Override
  public void enter() {
    Preconditions.checkState(getTurn().getStage().equals(TurnStage.ENEMY));
    super.enter();
    retaliate(world.getShipSnapshot(), 0);
  }

  /**
   * Now we code like Racket!
   */
  private void retaliate(final ImmutableList<Cell> shipSnapshot, final int i) {
    if (i == shipSnapshot.size()) {
      getTurn().advance();
      post(new Save());
      branchTo(stateFactory.createStartTurn());
      return;
    }

    final Runnable next = new Runnable() {
      @Override
      public void run() {
        retaliate(shipSnapshot, i + 1);
      }
    };

    Cell enemyCell = shipSnapshot.get(i);

    if (enemyCell.enemy().isPresent()) {
      final Enemy enemy = enemyCell.enemy().get();
      post(new ActivatedEnemy(enemy));
      handleMoving(enemy.getAutoPilot(movements, enemyCell), next);
    } else {
      next.run();
    }
  }

  private void handleMoving(final AutoPilot autoPilot, final Runnable next) {
    final Optional<Path> pathOpt = autoPilot.path();
    if (pathOpt.isPresent()) {
      Path path = pathOpt.get();
      path.getOrigin().moveShip(path).done(new Runnable() {
        @Override
        public void run() {
          handleBattle(autoPilot, next);
        }
      });
    } else {
      handleBattle(autoPilot, next);
    }
  }

  private void handleBattle(AutoPilot autoPilot, Runnable next) {
    if (autoPilot.battle().isPresent()) {
      battleSequence.start(autoPilot.battle().get(), next);
    } else {
      next.run();
    }
  }
}
