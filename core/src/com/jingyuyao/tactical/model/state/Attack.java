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
        return "attack";
    }

    @Override
    public void run() {
        // TODO: kick off battle animation somewhere
        // TODO: actual calculation time
        getState().getMarkings().unMarkEnemyDangerArea(targetEnemy);
        getState().getMap().kill(targetEnemy);
        getState().wait(attackingPlayer);
    }
}
