package com.jingyuyao.tactical.model.state;

import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.ModelBus;
import com.jingyuyao.tactical.model.battle.Battle;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.event.ActivatedEnemy;
import com.jingyuyao.tactical.model.world.Cell;
import com.jingyuyao.tactical.model.world.Movements;
import com.jingyuyao.tactical.model.world.World;
import javax.inject.Inject;

public class Retaliating extends BaseState {

  private final StateFactory stateFactory;
  private final Movements movements;
  private final Battle battle;
  private final World world;

  @Inject
  Retaliating(
      ModelBus modelBus,
      WorldState worldState,
      StateFactory stateFactory,
      Movements movements, Battle battle, World world) {
    super(modelBus, worldState);
    this.stateFactory = stateFactory;
    this.movements = movements;
    this.battle = battle;
    this.world = world;
  }

  @Override
  public void enter() {
    super.enter();
    retaliate(world.getCharacterSnapshot(), 0);
  }

  private void retaliate(final ImmutableList<Cell> characterSnapshot, final int i) {
    if (i == characterSnapshot.size()) {
      branchTo(stateFactory.createWaiting());
      return;
    }

    Cell cell = characterSnapshot.get(i);

    if (cell.enemy().isPresent()) {
      Enemy enemy = cell.enemy().get();
      post(new ActivatedEnemy(enemy));
      enemy.retaliate(movements, battle, cell).addCallback(new Runnable() {
        @Override
        public void run() {
          retaliate(characterSnapshot, i + 1);
        }
      });
    } else {
      retaliate(characterSnapshot, i + 1);
    }
  }
}
