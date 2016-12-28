package com.jingyuyao.tactical.model;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSet;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.object.Enemy;
import com.jingyuyao.tactical.model.object.Player;
import com.jingyuyao.tactical.model.object.Terrain;

public class AttackPlan {
    private final Map map;
    private final Player attackingPlayer;
    private final TargetInfo attackingPlayerTargetInfo;
    private final Enemy targetEnemy;
    /**
     * Never null.
     */
    private final Weapon playerWeapon;
    /**
     * Can be null.
     */
    private final Weapon enemyWeapon;
    private final Terrain playerTerrain;
    private final Terrain enemyTerrain;

    // TODO: get rid of player and enemy weapon and just use their equipped weapon
    public AttackPlan(Map map, Player attackingPlayer, TargetInfo attackingPlayerTargetInfo, Enemy targetEnemy) {
        Optional<Weapon> playerEquippedWeapon = attackingPlayer.getEquippedWeapon();
        // Not using Preconditions because Intellij is dumb
        if (!playerEquippedWeapon.isPresent()) {
            throw new IllegalArgumentException();
        }

        this.map = map;
        this.attackingPlayer = attackingPlayer;
        this.attackingPlayerTargetInfo = attackingPlayerTargetInfo;
        this.targetEnemy = targetEnemy;
        this.playerWeapon = playerEquippedWeapon.get();
        this.enemyWeapon = getHitBackWeapon(attackingPlayerTargetInfo, attackingPlayer, targetEnemy).orNull();
        this.playerTerrain = map.getTerrains().get(attackingPlayer.getCoordinate());
        this.enemyTerrain = map.getTerrains().get(targetEnemy.getCoordinate());
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

    private static Optional<Weapon> getHitBackWeapon(TargetInfo targetInfo, Player player, Enemy enemy) {
        Optional<Weapon> enemyEquippedWeapon = enemy.getEquippedWeapon();
        if (enemyEquippedWeapon.isPresent()) {
            ImmutableSet<Weapon> availableWeaponsForHittingBack =
                    targetInfo.weaponsFor(player.getCoordinate(), enemy.getCoordinate());
            if (availableWeaponsForHittingBack.contains(enemyEquippedWeapon.get())) {
                return enemyEquippedWeapon;
            }
        }
        return Optional.absent();
    }

    @Override
    public String toString() {
        return "AttackPlan{" +
                "attackingPlayer=" + attackingPlayer.getName() +
                ", targetEnemy=" + targetEnemy.getName() +
                '}';
    }
}
