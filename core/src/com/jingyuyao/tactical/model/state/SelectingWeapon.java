package com.jingyuyao.tactical.model.state;

import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.object.Enemy;
import com.jingyuyao.tactical.model.object.Player;
import com.jingyuyao.tactical.model.object.Terrain;

class SelectingWeapon extends AbstractState {
    private final Player attackingPlayer;
    private final Enemy targetEnemy;

    SelectingWeapon(AbstractState prevState, Player attackingPlayer, Enemy targetEnemy) {
        super(prevState);
        this.attackingPlayer = attackingPlayer;
        this.targetEnemy = targetEnemy;
    }

    @Override
    void enter() {
        // TODO: use a different marker for each stage
        getStateMarkings().showImmediateTargets(attackingPlayer);
    }

    @Override
    void canceled() {

    }

    @Override
    void exit() {
        getStateMarkings().clearPlayerMarking();
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
    ImmutableList<Action> getActions() {
        ImmutableList.Builder<Action> builder = new ImmutableList.Builder<Action>();
        ImmutableList<Weapon> availableWeapons = getMap()
                .getWeaponsForTarget(attackingPlayer, attackingPlayer.getCoordinate(), targetEnemy.getCoordinate());
        for (Weapon weapon : availableWeapons) {
            builder.add(new SelectWeapon(this, attackingPlayer, targetEnemy, weapon));
        }
        builder.add(new Back(this));
        return builder.build();
    }
}
