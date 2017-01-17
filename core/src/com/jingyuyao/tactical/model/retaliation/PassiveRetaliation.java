package com.jingyuyao.tactical.model.retaliation;

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.jingyuyao.tactical.model.character.Enemy;
import javax.inject.Singleton;

@Singleton
public class PassiveRetaliation implements Retaliation {

  @Override
  public ListenableFuture<Void> run(final Enemy enemy) {
    // TODO: current bugged due to weapon can hit owner
    /*
    Targets targets = enemy.createTargets();

    for (Terrain move : targets.moveTerrains()) {
      for (Weapon weapon : enemy.getWeapons()) {
        for (final Target target : weapon.createTargets(move.getCoordinate())) {
          if (!target.getTargetCharacters().isEmpty()) {
            Path path = targets.pathTo(move.getCoordinate());
            enemy.equipWeapon(weapon);
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
    */
    return Futures.immediateFuture(null);
  }
}
