package com.jingyuyao.tactical.model;

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

    @Override
    public String toString() {
        return "AttackPlan{" +
                "attackPlayer=" + attackPlayer +
                ", targetEnemy=" + targetEnemy +
                ", playerWeapon=" + playerWeapon +
                '}';
    }
}
