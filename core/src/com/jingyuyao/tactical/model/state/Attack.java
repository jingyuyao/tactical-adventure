package com.jingyuyao.tactical.model.state;

import com.jingyuyao.tactical.model.Enemy;
import com.jingyuyao.tactical.model.Player;
import com.jingyuyao.tactical.model.Weapon;

public class Attack extends AbstractAction {
    private final Player attackingPlayer;
    private final Enemy targetEnemy;
    private final Weapon playerWeapon;

    Attack(AbstractState state, Player attackingPlayer, Enemy targetEnemy, Weapon playerWeapon) {
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
        // TODO: actual calculation time
        getState().getMap().kill(targetEnemy);
        // TODO: oh man, we shouldn't have to remove twice...
        getState().getMarkings().removeEnemy(targetEnemy);
        getState().wait(attackingPlayer);
    }
}
