package com.jingyuyao.tactical.model.character;

import com.jingyuyao.tactical.model.battle.Battle;
import com.jingyuyao.tactical.model.event.MyFuture;
import com.jingyuyao.tactical.model.item.Target;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.world.Cell;
import com.jingyuyao.tactical.model.world.Movement;
import com.jingyuyao.tactical.model.world.Movements;
import com.jingyuyao.tactical.model.world.Path;

public class PassiveEnemy extends BaseCharacter implements Enemy {

  PassiveEnemy() {
  }

  @Override
  public MyFuture retaliate(Movements movements, final Battle battle, Cell startingCell) {
    Movement movement = movements.distanceFrom(startingCell);
    for (Cell moveCell : movement.getCells()) {
      for (final Weapon weapon : getWeapons()) {
        for (final Target target : weapon.createTargets(movements, moveCell)) {
          boolean containsPlayer = false;
          boolean containsEnemy = false;

          for (Cell cell : target.getTargetCells()) {
            if (cell.player().isPresent()) {
              containsPlayer = true;
            }

            if (cell.enemy().isPresent()) {
              containsEnemy = true;
            }
          }

          // Don't hit friendly characters?
          if (!containsEnemy && containsPlayer) {
            Path path = movement.pathTo(moveCell);

            final MyFuture future = new MyFuture();
            startingCell.moveCharacter(path).addCallback(new Runnable() {
              @Override
              public void run() {
                battle.begin(PassiveEnemy.this, weapon, target).addCallback(new Runnable() {
                  @Override
                  public void run() {
                    future.done();
                  }
                });
              }
            });
            return future;
          }
        }
      }
    }
    return MyFuture.immediate();
  }
}
