package com.jingyuyao.tactical.model.state;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.Enemy;
import com.jingyuyao.tactical.model.Player;
import com.jingyuyao.tactical.model.Terrain;
import com.jingyuyao.tactical.model.Weapon;

class ReviewingAttack extends AbstractState {
    private final Player attackingPlayer;
    private final Enemy targetEnemy;
    private final Weapon playerWeapon;

    ReviewingAttack(AbstractState prevState, Player attackingPlayer, Enemy targetEnemy, Weapon playerWeapon) {
        super(prevState);
        this.attackingPlayer = attackingPlayer;
        this.targetEnemy = targetEnemy;
        this.playerWeapon = playerWeapon;
    }

    @Override
    void enter() {
        // TODO: use a different marker for each stage
        getMarkings().markEnemyTarget(attackingPlayer, targetEnemy);
        // TODO: how do we sent info to ui?
    }

    @Override
    void canceled() {

    }

    @Override
    void exit() {
        getMarkings().unMarkLastPlayer();
    }

    @Override
    void select(Player player) {
        back();
    }

    @Override
    void select(Enemy enemy) {
        back();
    }

    @Override
    void select(Terrain terrain) {
        back();
    }

    @Override
    ImmutableCollection<Action> getActions() {
        return ImmutableList.<Action>of(
                new Attack(this, attackingPlayer, targetEnemy, playerWeapon),
                new Back(this)
        );
    }
}
