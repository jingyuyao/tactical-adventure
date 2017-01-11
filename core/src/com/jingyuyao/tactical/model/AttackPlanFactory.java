package com.jingyuyao.tactical.model;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.map.Targets;
import com.jingyuyao.tactical.model.map.TargetsFactory;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AttackPlanFactory {

  private final TargetsFactory targetsFactory;

  @Inject
  public AttackPlanFactory(TargetsFactory targetsFactory) {
    this.targetsFactory = targetsFactory;
  }

  public AttackPlan create(Player player, Enemy enemy) {
    Optional<Weapon> playerWeapon = player.getEquippedWeapon();
    Preconditions.checkArgument(playerWeapon.isPresent());

    Targets playerTargets = targetsFactory.create(player);
    Preconditions.checkArgument(playerTargets.immediate().canTarget(enemy));

    Targets enemyTargets = targetsFactory.create(enemy);
    Optional<Weapon> enemyWeapon = getHitBackWeapon(player, enemy, enemyTargets);

    return new AttackPlan(player, enemy, playerWeapon.get(), enemyWeapon.orNull());
  }

  private Optional<Weapon> getHitBackWeapon(Player player, Enemy enemy, Targets enemyTargets) {
    Optional<Weapon> enemyEquippedWeapon = enemy.getEquippedWeapon();
    if (enemyEquippedWeapon.isPresent()) {
      ImmutableSet<Weapon> availableWeaponsForHittingBack =
          enemyTargets.availableWeapons(enemy.getCoordinate(), player.getCoordinate());
      if (availableWeaponsForHittingBack.contains(enemyEquippedWeapon.get())) {
        return enemyEquippedWeapon;
      }
    }
    return Optional.absent();
  }
}
