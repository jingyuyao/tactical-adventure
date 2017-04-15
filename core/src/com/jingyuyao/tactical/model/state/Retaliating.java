package com.jingyuyao.tactical.model.state;

import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.model.ModelModule.ModelEventBus;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.event.ActivatedEnemy;
import com.jingyuyao.tactical.model.world.Cell;
import com.jingyuyao.tactical.model.world.World;
import javax.inject.Inject;

public class Retaliating extends BaseState {

  private final StateFactory stateFactory;
  private final World world;

  @Inject
  Retaliating(
      @ModelEventBus EventBus eventBus,
      WorldState worldState,
      StateFactory stateFactory,
      World world) {
    super(eventBus, worldState);
    this.stateFactory = stateFactory;
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
    if (!cell.hasEnemy()) {
      retaliate(characterSnapshot, i + 1);
      return;
    }

    Enemy enemy = cell.getEnemy();
    post(new ActivatedEnemy(enemy));
    enemy.retaliate(cell).addCallback(new Runnable() {
      @Override
      public void run() {
        retaliate(characterSnapshot, i + 1);
      }
    });
  }
}
