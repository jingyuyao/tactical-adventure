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
        getMarkings().unMarkPlayer();
    }

    @Override
    void select(Player player) {
        goToPrevState();
    }

    @Override
    void select(Enemy enemy) {
        goToPrevState();
    }

    @Override
    void select(Terrain terrain) {
        goToPrevState();
    }

    @Override
    ImmutableCollection<Action> getActions() {
        ImmutableList.Builder<Action> builder = ImmutableList.builder();
        builder.add(new Cancel(this));
        builder.add(new Finish(this, currentPlayer));
        if (getMap().hasAnyImmediateTarget(currentPlayer)) {
            builder.add(new Action() {
                @Override
                public String getName() {
                    return "Attack";
                }

                @Override
                public void run() {
                    goTo(new Targeting(Choosing.this, currentPlayer));
                }
            });
        }
        // TODO: add use items action
        return builder.build();
    }
}
