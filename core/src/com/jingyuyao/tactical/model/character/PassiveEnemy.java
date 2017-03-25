package com.jingyuyao.tactical.model.character;

import com.google.common.collect.FluentIterable;
import com.google.common.util.concurrent.AsyncFunction;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.jingyuyao.tactical.model.battle.Battle;
import com.jingyuyao.tactical.model.item.Item;
import com.jingyuyao.tactical.model.item.Target;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.map.Cell;
import com.jingyuyao.tactical.model.map.Movement;
import com.jingyuyao.tactical.model.map.Movements;
import com.jingyuyao.tactical.model.map.Path;
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
  public ListenableFuture<Void> retaliate(Cell startingCell) {
    Movement movement = movements.distanceFrom(startingCell);
    for (Cell moveCell : movement.getCells()) {
      for (final Weapon weapon : fluentItems().filter(Weapon.class)) {
        for (final Target target : weapon.createTargets(moveCell)) {
          FluentIterable<Character> targetCharacters = target.getTargetCharacters();
          // Don't hit friendly characters?
          if (!targetCharacters.filter(Enemy.class).isEmpty()) {
            continue;
          }
          if (!targetCharacters.filter(Player.class).isEmpty()) {
            Path path = movement.pathTo(moveCell);

            return Futures
                .transformAsync(startingCell.moveCharacter(path), new AsyncFunction<Void, Void>() {
                  @Override
                  public ListenableFuture<Void> apply(Void input) {
                    return battle.begin(PassiveEnemy.this, weapon, target);
                  }
                });
          }
        }
      }
    }
    return Futures.immediateFuture(null);
  }
}
