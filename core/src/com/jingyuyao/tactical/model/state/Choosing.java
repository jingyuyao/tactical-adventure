package com.jingyuyao.tactical.model.state;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.jingyuyao.tactical.model.object.Enemy;
import com.jingyuyao.tactical.model.object.Player;
import com.jingyuyao.tactical.model.object.Terrain;

class Choosing extends AbstractPlayerState {
    Choosing(AbstractPlayerState prevState) {
        super(prevState);
    }

    @Override
    void enter() {
        super.enter();
        getMarkings().showImmediateTargets(getTargetInfo());
    }

    @Override
    void canceled() {}

    @Override
    public void select(Player player) {
        if (Objects.equal(getCurrentPlayer(), player)) {
            back();
        } else {
            goTo(new Moving(backToWaiting(), player));
        }
    }

    @Override
    public void select(Enemy enemy) {
        if (getTargetInfo().canHitImmediately(enemy)) {
            goTo(new SelectingWeapon(this, enemy));
        } else {
            back();
        }
    }

    @Override
    public void select(Terrain terrain) {
        back();
    }

    @Override
    public ImmutableList<Action> getActions() {
        ImmutableList.Builder<Action> builder = new ImmutableList.Builder<Action>();
        if (!Iterables.isEmpty(getCurrentPlayer().getConsumables())) {
            builder.add(new ChooseItemToUse(this, getCurrentPlayer()));
        }
        builder.add(new Wait(this, getCurrentPlayer()));
        builder.add(new Back(this));
        return builder.build();
    }
}
