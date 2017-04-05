package com.jingyuyao.tactical.model.character;

import com.jingyuyao.tactical.model.battle.Battle;
import com.jingyuyao.tactical.model.event.MyFuture;
import com.jingyuyao.tactical.model.item.Item;
import com.jingyuyao.tactical.model.item.Target;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.world.Cell;
import com.jingyuyao.tactical.model.world.Movement;
import com.jingyuyao.tactical.model.world.Movements;
import com.jingyuyao.tactical.model.world.Path;
import java.util.List;
import javax.inject.Inject;

public class PassiveEnemy extends AbstractEnemy {

  private final transient Movements movements;
  private final transient Battle battle;

  @Inject
  PassiveEnemy(Movements movements, Battle battle) {
    this.movements = movements;
    this.battle = battle;
  }

  PassiveEnemy(
      Movements movements, Battle battle, String name, int maxHp, int hp, int moveDistance,
      List<Item> items) {
    super(name, maxHp, hp, moveDistance, items);
    this.movements = movements;
    this.battle = battle;
  }

  @Override
  public MyFuture retaliate(Cell startingCell) {
    Movement movement = movements.distanceFrom(startingCell);
    for (Cell moveCell : movement.getCells()) {
      for (final Weapon weapon : fluentItems().filter(Weapon.class)) {
        for (final Target target : weapon.createTargets(moveCell)) {
          boolean containsPlayer = false;
          boolean containsEnemy = false;

          for (Cell cell : target.getTargetCells()) {
            if (cell.hasPlayer()) {
              containsPlayer = true;
            } else if (cell.hasEnemy()) {
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
