package com.jingyuyao.tactical.model.state;

import com.google.common.collect.ImmutableCollection;
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
        goTo(new Moving(backToWaiting(), player));
    }

    @Override
    void select(Enemy enemy) {
        if (getMap().canImmediateTarget(currentPlayer, enemy)) {
            goTo(new BattlePrepping(this, currentPlayer, enemy));
        } else {
            backToWaiting();
        }
    }

    @Override
    void select(Terrain terrain) {
        backToWaiting();
    }

    @Override
    ImmutableCollection<Action> getActions() {
        ImmutableList.Builder<Action> builder = ImmutableList.builder();
        builder.add(new Back(this));
        builder.add(new Done(this, currentPlayer));
        // TODO: add use items action
        return builder.build();
    }
}
