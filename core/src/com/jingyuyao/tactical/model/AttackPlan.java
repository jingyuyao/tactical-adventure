package com.jingyuyao.tactical.model;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.object.Enemy;
import com.jingyuyao.tactical.model.object.Player;

public class AttackPlan {
    private final Player attackingPlayer;
    private final Enemy targetEnemy;
    /**
     * Never null.
     */
    private final Weapon playerWeapon;
    /**
     * Can be null.
     */
    private final Weapon enemyWeapon;

    // TODO: add terrains
    private AttackPlan(Player attackingPlayer, Enemy targetEnemy, Weapon playerWeapon, Weapon enemyWeapon) {
        this.attackingPlayer = attackingPlayer;
        this.targetEnemy = targetEnemy;
        this.playerWeapon = playerWeapon;
        this.enemyWeapon = enemyWeapon;
    }

    public Player getAttackingPlayer() {
        return attackingPlayer;
    }

    public Enemy getTargetEnemy() {
        return targetEnemy;
    }

    /**
     * Executes this attack plan, make HP reduction calculations
     */
    public void execute() {
        // TODO: complete me
        targetEnemy.damageBy(playerWeapon.getAttackPower());
        playerWeapon.useOnce();

        if (targetEnemy.isAlive() && enemyWeapon != null) {
            attackingPlayer.damageBy(enemyWeapon.getAttackPower());
            enemyWeapon.useOnce();
        }
    }

    @Override
    public String toString() {
        return "AttackPlan{" +
                "attackingPlayer=" + attackingPlayer.getName() +
                ", targetEnemy=" + targetEnemy.getName() +
                '}';
    }

    public static class Factory {
        private final TargetInfo.Factory targetInfoFactory;

        public Factory(TargetInfo.Factory targetInfoFactory) {
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
}
