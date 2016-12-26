package com.jingyuyao.tactical.model.state;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.AttackPlan;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.object.Enemy;
import com.jingyuyao.tactical.model.object.Player;

import java.util.Locale;

/**
 * Equips {@link #playerWeapon} then go to {@link ReviewingAttack} state.
 */
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
        return String.format(Locale.US, "%s (%d)", playerWeapon.getName(), playerWeapon.getUsageLeft());
    }

    @Override
    public void run() {
        attackingPlayer.equipWeapon(playerWeapon);
        Weapon hitBackWeapon = null;
        Optional<Weapon> enemyEquippedWeapon = targetEnemy.getEquippedWeapon();
        if (enemyEquippedWeapon.isPresent()) {
            ImmutableList<Weapon> availableWeaponsForHittingBack =
                    getState().getMap().getWeaponsForTarget(
                            targetEnemy, targetEnemy.getCoordinate(), attackingPlayer.getCoordinate());
            if (availableWeaponsForHittingBack.contains(enemyEquippedWeapon.get())) {
                hitBackWeapon = enemyEquippedWeapon.get();
            }
        }

        getState().goTo(
                new ReviewingAttack(
                        getState(),
                        new AttackPlan(attackingPlayer, targetEnemy, playerWeapon, hitBackWeapon)
                )
        );
    }
}
