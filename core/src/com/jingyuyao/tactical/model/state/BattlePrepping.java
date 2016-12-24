package com.jingyuyao.tactical.model.state;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.Enemy;
import com.jingyuyao.tactical.model.Player;
import com.jingyuyao.tactical.model.Terrain;
import com.jingyuyao.tactical.model.Weapon;

class BattlePrepping extends AbstractState {
    private final Player attackingPlayer;
    private final Enemy targetEnemy;

    BattlePrepping(AbstractState prevState, Player attackingPlayer, Enemy targetEnemy) {
        super(prevState);
        this.attackingPlayer = attackingPlayer;
        this.targetEnemy = targetEnemy;
    }

    @Override
    void enter() {
        getMarkings().markEnemyTarget(attackingPlayer, targetEnemy);
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
        ImmutableList.Builder<Action> builder = new ImmutableList.Builder<Action>();
        ImmutableList<Weapon> availableWeapons = getMap()
                .getWeaponsForTarget(attackingPlayer, attackingPlayer.getCoordinate(), targetEnemy.getCoordinate());
        for (Weapon weapon : availableWeapons) {
            builder.add(new Attack(this, attackingPlayer, targetEnemy, weapon));
        }
        builder.add(new Back(this));
        return builder.build();
    }
}
