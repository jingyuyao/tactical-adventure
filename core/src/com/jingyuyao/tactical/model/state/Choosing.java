package com.jingyuyao.tactical.model.state;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.Coordinate;
import com.jingyuyao.tactical.model.Enemy;
import com.jingyuyao.tactical.model.Player;
import com.jingyuyao.tactical.model.Terrain;

import java.util.Collection;
import java.util.Observable;
import java.util.Observer;

class Choosing extends AbstractState {
    private final Player currentPlayer;

    Choosing(AbstractState prevState, Player currentPlayer) {
        super(prevState);
        this.currentPlayer = currentPlayer;
    }

    @Override
    void enter() {
        if (getAnimationCounter().isAnimating()) {
            getAnimationCounter().addObserver(this.new WaitToMark());
        } else {
            markIfHasTarget();
        }
    }

    @Override
    void canceled() {
        currentPlayer.moveBack();
    }

    @Override
    void exit() {
        getMarkings().unMarkLastPlayer();
    }

    @Override
    void select(Player player) {
        hardCancel();
    }

    @Override
    void select(Enemy enemy) {
        if (getMap().canImmediateTarget(currentPlayer, enemy)) {
            goTo(new BattlePrep(this, currentPlayer, enemy));
        } else {
            hardCancel();
        }
    }

    @Override
    void select(Terrain terrain) {
        hardCancel();
    }

    @Override
    ImmutableCollection<Action> getActions() {
        ImmutableList.Builder<Action> builder = ImmutableList.builder();
        builder.add(new Back(this));
        builder.add(new Finish(this, currentPlayer));
        // TODO: add use items action
        return builder.build();
    }

    private void markIfHasTarget() {
        if (getMap().hasAnyImmediateTarget(currentPlayer)) {
            getMarkings().markPlayer(currentPlayer, true);
        }
    }

    private class WaitToMark implements Observer {
        @Override
        public void update(Observable observable, Object o) {
            if (!getAnimationCounter().isAnimating()) {
                markIfHasTarget();
                getAnimationCounter().deleteObserver(this);
            }
        }
    }
}
