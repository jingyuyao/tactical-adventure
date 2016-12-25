package com.jingyuyao.tactical.model;

import com.google.common.base.Preconditions;
import com.jingyuyao.tactical.model.object.Enemy;
import com.jingyuyao.tactical.model.object.Player;

public class AttackPlan {
    // TODO: add the terrain they are standing on
    private final Player attackingPlayer;
    private final Enemy targetEnemy;

    public AttackPlan(Player attackingPlayer, Enemy targetEnemy) {
        this.attackingPlayer = attackingPlayer;
        this.targetEnemy = targetEnemy;
        Preconditions.checkNotNull(attackingPlayer.getItems().getEquippedWeapon());
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
        targetEnemy.damageBy(attackingPlayer.getItems().getEquippedWeapon().getAttackPower());
    }

    @Override
    public String toString() {
        return "AttackPlan{" +
                "attackingPlayer=" + attackingPlayer.getName() +
                ", targetEnemy=" + targetEnemy.getName() +
                '}';
    }
}
