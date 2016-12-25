package com.jingyuyao.tactical.model.state;

import com.jingyuyao.tactical.model.AttackPlan;
import com.jingyuyao.tactical.model.Enemy;
import com.jingyuyao.tactical.model.Player;

public class SelectWeapon extends AbstractAction {
    private final Player attackingPlayer;
    private final Enemy targetEnemy;
    private final com.jingyuyao.tactical.model.item.Weapon playerWeapon;

    SelectWeapon(AbstractState state, Player attackingPlayer, Enemy targetEnemy, com.jingyuyao.tactical.model.item.Weapon playerWeapon) {
        super(state);
        this.attackingPlayer = attackingPlayer;
        this.targetEnemy = targetEnemy;
        this.playerWeapon = playerWeapon;
    }

    @Override
    public String getName() {
        return playerWeapon.getName();
    }

    @Override
    public void run() {
        getState().goTo(new ReviewingAttack(getState(), new AttackPlan(attackingPlayer, targetEnemy, playerWeapon)));
    }
}
