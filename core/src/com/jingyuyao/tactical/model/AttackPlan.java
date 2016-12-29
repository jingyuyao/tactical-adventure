package com.jingyuyao.tactical.model;

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
    AttackPlan(Player attackingPlayer, Enemy targetEnemy, Weapon playerWeapon, Weapon enemyWeapon) {
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

}
