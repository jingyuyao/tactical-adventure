package com.jingyuyao.tactical.model;

import com.jingyuyao.tactical.model.item.Weapon;

public class AttackPlan {
    private final Player attackPlayer;
    private final Enemy targetEnemy;
    private final Weapon playerWeapon;

    public AttackPlan(Player attackPlayer, Enemy targetEnemy, Weapon playerWeapon) {
        this.attackPlayer = attackPlayer;
        this.targetEnemy = targetEnemy;
        this.playerWeapon = playerWeapon;
    }

    public Player getAttackPlayer() {
        return attackPlayer;
    }

    public Enemy getTargetEnemy() {
        return targetEnemy;
    }

    public Weapon getPlayerWeapon() {
        return playerWeapon;
    }

    /**
     * Executes this attack plan, make HP reduction calculations, call die() if character dead.
     */
    public void execute() {
        // TODO: implement me
    }

    @Override
    public String toString() {
        return "AttackPlan{" +
                "attackPlayer=" + attackPlayer.getName() +
                ", targetEnemy=" + targetEnemy.getName() +
                ", playerWeapon=" + playerWeapon.getName() +
                '}';
    }
}
