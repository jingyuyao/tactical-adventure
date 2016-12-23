package com.jingyuyao.tactical.model.state;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.Coordinate;
import com.jingyuyao.tactical.model.Enemy;
import com.jingyuyao.tactical.model.Player;
import com.jingyuyao.tactical.model.Terrain;

import java.util.Collection;

class Choosing extends AbstractState {
    private final Player currentPlayer;

    Choosing(AbstractState prevState, Player currentPlayer) {
        super(prevState);
        this.currentPlayer = currentPlayer;
    }

    @Override
    void enter() {
        if (getMap().hasAnyImmediateTarget(currentPlayer)) {
            getMarkings().markPlayer(currentPlayer, true);
        }
    }

    @Override
    void canceled() {
        // TODO: clean me up
        Collection<Coordinate> lastPath = currentPlayer.getLastPath();
        if (lastPath.size() > 1) {
            Coordinate previousCoordinate = lastPath.iterator().next();
            if (!previousCoordinate.equals(currentPlayer.getCoordinate())) {
                getMap().moveIfAble(currentPlayer, previousCoordinate);
                currentPlayer.getLastPath().clear();
            }
        }
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
            // TODO: enter battle prep
            getMarkings().removeEnemy(enemy);
            getMap().kill(enemy);
            finish(currentPlayer);
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
}
