package com.jingyuyao.tactical.model;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.map.TargetInfo;
import com.jingyuyao.tactical.model.map.TargetInfoFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AttackPlanFactory {
    private final TargetInfoFactory targetInfoFactory;

    @Inject
    public AttackPlanFactory(TargetInfoFactory targetInfoFactory) {
        this.targetInfoFactory = targetInfoFactory;
    }

    public AttackPlan create(Player player, Weapon playerWeapon, Enemy enemy) {
        Preconditions.checkArgument(Iterables.contains(player.getWeapons(), playerWeapon));

        TargetInfo playerInfo = targetInfoFactory.create(player);
        Preconditions.checkArgument(playerInfo.canHitImmediately(enemy));

        TargetInfo enemyInfo = targetInfoFactory.create(enemy);
        Optional<Weapon> enemyWeapon = getHitBackWeapon(player, enemy, enemyInfo);

        return new AttackPlan(player, enemy, playerWeapon, enemyWeapon.orNull());
    }

    private Optional<Weapon> getHitBackWeapon(Player player, Enemy enemy, TargetInfo enemyInfo) {
        Optional<Weapon> enemyEquippedWeapon = enemy.getEquippedWeapon();
        if (enemyEquippedWeapon.isPresent()) {
            ImmutableSet<Weapon> availableWeaponsForHittingBack =
                    enemyInfo.weaponsFor(enemy.getCoordinate(), player.getCoordinate());
            if (availableWeaponsForHittingBack.contains(enemyEquippedWeapon.get())) {
                return enemyEquippedWeapon;
            }
        }
        return Optional.absent();
    }
}
