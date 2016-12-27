package com.jingyuyao.tactical.model.state;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.jingyuyao.tactical.model.object.Enemy;
import com.jingyuyao.tactical.model.object.Player;
import com.jingyuyao.tactical.model.object.Terrain;

class Choosing extends AbstractState {
    private final Player currentPlayer;

    Choosing(AbstractState prevState, Player currentPlayer) {
        super(prevState);
        this.currentPlayer = currentPlayer;
    }

    @Override
    void enter() {
        if (getMap().hasAnyImmediateTarget(currentPlayer)) {
            getStateMarkings().showImmediateTargets(currentPlayer);
        }
    }

    @Override
    void canceled() {}

    @Override
    void exit() {
        getStateMarkings().clearPlayerMarking();
    }

    @Override
    void select(Player player) {
        if (Objects.equal(currentPlayer, player)) {
            back();
        } else {
            goTo(new Moving(backToWaiting(), player));
        }
    }

    @Override
    void select(Enemy enemy) {
        if (getMap().canImmediateTarget(currentPlayer, enemy)) {
            goTo(new SelectingWeapon(this, currentPlayer, enemy));
        } else {
            back();
        }
    }

    @Override
    void select(Terrain terrain) {
        back();
    }

    @Override
    ImmutableList<Action> getActions() {
        ImmutableList.Builder<Action> builder = new ImmutableList.Builder<Action>();
        if (!Iterables.isEmpty(currentPlayer.getConsumables())) {
            builder.add(new ChooseItemToUse(this, currentPlayer));
        }
        builder.add(new Wait(this, currentPlayer));
        builder.add(new Back(this));
        return builder.build();
    }
}
