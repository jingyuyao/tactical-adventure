package com.jingyuyao.tactical.model.state;

import com.jingyuyao.tactical.model.*;

public class SelectWeapon extends AbstractAction {
    private final Player attackingPlayer;
    private final Enemy targetEnemy;
    private final Weapon playerWeapon;

    SelectWeapon(AbstractState state, Player attackingPlayer, Enemy targetEnemy, Weapon playerWeapon) {
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
        getState().goTo(new ReviewingAttack(getState(), new AttackInfo(attackingPlayer, targetEnemy, playerWeapon)));
    }
}
