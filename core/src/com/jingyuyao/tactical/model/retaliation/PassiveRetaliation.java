package com.jingyuyao.tactical.model.retaliation;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.jingyuyao.tactical.model.battle.Target;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.common.Coordinate;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.map.Movement;
import com.jingyuyao.tactical.model.map.MovementFactory;
import com.jingyuyao.tactical.model.map.Path;
import com.jingyuyao.tactical.model.map.Terrain;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PassiveRetaliation implements Retaliation {

  private final MovementFactory movementFactory;

  @Inject
  PassiveRetaliation(MovementFactory movementFactory) {
    this.movementFactory = movementFactory;
  }

  @Override
  public ListenableFuture<Void> run(final Enemy enemy) {
    Movement targets = movementFactory.create(enemy);
    Coordinate originalCoordinate = enemy.getCoordinate();

    for (Terrain move : targets.getTerrains()) {
      enemy.setCoordinate(move.getCoordinate());
      for (Weapon weapon : enemy.getWeapons()) {
        for (final Target target : weapon.createTargets(enemy)) {
          ImmutableList<Character> targetCharacters = target.getTargetCharacters();
          // Don't hit friendly characters?
          if (!Iterables.isEmpty(Iterables.filter(targetCharacters, Enemy.class))) {
            continue;
          }
          boolean hasPlayers = !Iterables.isEmpty(Iterables.filter(targetCharacters, Player.class));

          if (hasPlayers) {
            enemy.setCoordinate(originalCoordinate);
            Path path = targets.pathTo(move.getCoordinate());
            ListenableFuture<Void> future = enemy.move(path);
            Futures.addCallback(future, new FutureCallback<Void>() {
              @Override
              public void onSuccess(Void result) {
                target.execute();
              }

              @Override
              public void onFailure(Throwable t) {

              }
            });
            return future;
          }
        }
      }
    }
    enemy.setCoordinate(originalCoordinate);
    return Futures.immediateFuture(null);
  }
}
