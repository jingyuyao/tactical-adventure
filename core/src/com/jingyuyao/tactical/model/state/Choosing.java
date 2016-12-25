package com.jingyuyao.tactical.model.state;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.Enemy;
import com.jingyuyao.tactical.model.Player;
import com.jingyuyao.tactical.model.Terrain;

class Choosing extends AbstractState {
    private final Player currentPlayer;

    Choosing(AbstractState prevState, Player currentPlayer) {
        super(prevState);
        this.currentPlayer = currentPlayer;
    }

    @Override
    void enter() {
        if (getMap().hasAnyImmediateTarget(currentPlayer)) {
            getMarkings().markImmediateTargets(currentPlayer);
        }
    }

    @Override
    void canceled() {}

    @Override
    void exit() {
        getMarkings().unMarkLastPlayer();
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
        // TODO: add use items action
        return ImmutableList.<Action>of(
                new Wait(this, currentPlayer),
                new Back(this)
        );
    }
}
