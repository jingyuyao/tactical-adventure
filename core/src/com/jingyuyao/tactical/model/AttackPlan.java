package com.jingyuyao.tactical.model;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.object.Enemy;
import com.jingyuyao.tactical.model.object.Player;
import com.jingyuyao.tactical.model.object.Terrain;

public class AttackPlan {
    private final Map map;
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
    private final Terrain playerTerrain;
    private final Terrain enemyTerrain;

    // TODO: get rid of player and enemy weapon and just use their equipped weapon
    public AttackPlan(Map map, Player attackingPlayer, Enemy targetEnemy) {
        Optional<Weapon> playerEquippedWeapon = attackingPlayer.getEquippedWeapon();
        // Not using Preconditions because Intellij is dumb
        if (!playerEquippedWeapon.isPresent()) {
            throw new IllegalArgumentException();
        }

        this.map = map;
        this.attackingPlayer = attackingPlayer;
        this.targetEnemy = targetEnemy;
        this.playerWeapon = playerEquippedWeapon.get();
        this.enemyWeapon = getHitBackWeapon(map, attackingPlayer, targetEnemy).orNull();
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

    private static Optional<Weapon> getHitBackWeapon(Map map, Player player, Enemy enemy) {
        Optional<Weapon> enemyEquippedWeapon = enemy.getEquippedWeapon();
        if (enemyEquippedWeapon.isPresent()) {
            ImmutableList<Weapon> availableWeaponsForHittingBack =
                    map.getWeaponsForTarget(
                            enemy, enemy.getCoordinate(), player.getCoordinate());
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
