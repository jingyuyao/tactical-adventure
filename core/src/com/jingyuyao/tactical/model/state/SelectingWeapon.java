package com.jingyuyao.tactical.model.state;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.jingyuyao.tactical.model.action.Action;
import com.jingyuyao.tactical.model.action.Back;
import com.jingyuyao.tactical.model.action.SelectWeapon;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.object.Enemy;

class SelectingWeapon extends AbstractPlayerState {
    private final Enemy targetEnemy;

    SelectingWeapon(AbstractPlayerState prevState, Enemy targetEnemy) {
        super(prevState);
        this.targetEnemy = targetEnemy;
    }

    @Override
    public void enter() {
        super.enter();
        // TODO: use a different marker for each stage
        getMarkings().showImmediateTargets(getTargetInfo());
    }

    @Override
    public ImmutableList<Action> getActions() {
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
