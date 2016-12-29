package com.jingyuyao.tactical.model.action;

import com.jingyuyao.tactical.model.AttackPlan;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.object.Player;
import com.jingyuyao.tactical.model.state.AbstractState;
import com.jingyuyao.tactical.model.state.ReviewingAttack;

import java.util.Locale;

/**
 * Equips {@link #playerWeapon} then go to {@link ReviewingAttack} state.
 */
public class SelectWeapon extends AbstractAction {
    private final Player attackingPlayer;
    private final Weapon playerWeapon;
    private final AttackPlan attackPlan;

    public SelectWeapon(AbstractState state, Player attackingPlayer, Weapon playerWeapon, AttackPlan attackPlan) {
        super(state);
        this.attackingPlayer = attackingPlayer;
        this.playerWeapon = playerWeapon;
        this.attackPlan = attackPlan;
    }

    @Override
    public String getName() {
        return String.format(Locale.US, "%s (%d)", playerWeapon.getName(), playerWeapon.getUsageLeft());
    }

    @Override
    public void run() {
        attackingPlayer.equipWeapon(playerWeapon);
        getState().goTo(new ReviewingAttack(
                getState(),
                attackingPlayer,
                attackPlan
        ));
    }
}
