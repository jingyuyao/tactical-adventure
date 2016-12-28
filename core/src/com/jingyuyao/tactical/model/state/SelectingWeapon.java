package com.jingyuyao.tactical.model.state;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.object.Enemy;
import com.jingyuyao.tactical.model.object.Player;
import com.jingyuyao.tactical.model.object.Terrain;

class SelectingWeapon extends AbstractPlayerState {
    private final Enemy targetEnemy;

    SelectingWeapon(AbstractPlayerState prevState, Enemy targetEnemy) {
        super(prevState);
        this.targetEnemy = targetEnemy;
    }

    @Override
    void enter() {
        super.enter();
        // TODO: use a different marker for each stage
        getStateMarkings().showImmediateTargets(getTargetInfo());
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
        ImmutableSet<Weapon> availableWeapons = getTargetInfo()
                .weaponsFor(getCurrentPlayer().getCoordinate(), targetEnemy.getCoordinate());
        for (Weapon weapon : availableWeapons) {
            builder.add(new SelectWeapon(this, getCurrentPlayer(), targetEnemy, weapon));
        }
        builder.add(new Back(this));
        return builder.build();
    }
}
