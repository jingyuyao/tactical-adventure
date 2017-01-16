package com.jingyuyao.tactical.model.retaliation;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.jingyuyao.tactical.model.AttackPlanFactory;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.map.Path;
import com.jingyuyao.tactical.model.map.Targets;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PassiveRetaliation implements Retaliation {

  private final AttackPlanFactory attackPlanFactory;

  @Inject
  PassiveRetaliation(AttackPlanFactory attackPlanFactory) {
    this.attackPlanFactory = attackPlanFactory;
  }

  @Override
  public ListenableFuture<Void> run(final Enemy enemy) {
    Targets targets = enemy.createTargets();
    List<Player> targetablePlayers =
        Lists.newArrayList(Iterables.filter(targets.all().characters(), Player.class));

    if (!targetablePlayers.isEmpty()) {
      sortTargetablePlayers(targetablePlayers);

      for (final Player player : targetablePlayers) {
        Path path = targets.movePathToTargetCoordinate(player.getCoordinate());
        for (Weapon weapon : enemy.getWeapons()) {
          if (weapon.canHitFrom(enemy, path.getDestination(), player)) {
            enemy.equipWeapon(weapon);
            ListenableFuture<Void> future = enemy.move(path);
            Futures.addCallback(future, new FutureCallback<Void>() {
              @Override
              public void onSuccess(Void result) {
                attackPlanFactory.create(enemy, player).execute();
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
    return Futures.immediateFuture(null);
  }

  private void sortTargetablePlayers(List<Player> targetablePlayers) {
    Collections.sort(targetablePlayers, new Comparator<Player>() {
      @Override
      public int compare(Player p1, Player p2) {
        return p1.getHp() - p2.getHp();
      }
    });
  }
}
