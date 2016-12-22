package com.jingyuyao.tactical.model.state;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.Coordinate;
import com.jingyuyao.tactical.model.Enemy;
import com.jingyuyao.tactical.model.Player;
import com.jingyuyao.tactical.model.Terrain;

class Moving extends AbstractState {
    private final Player currentPlayer;

    Moving(AbstractState prevState, Player currentPlayer) {
        super(prevState);
        this.currentPlayer = currentPlayer;
    }

    @Override
    void enter() {
        getMarkings().markPlayer(currentPlayer, false);
    }

    @Override
    void canceled() {}

    @Override
    void exit() {
        getMarkings().unMarkPlayer();
    }

    @Override
    public void select(Player player) {
        if (Objects.equal(currentPlayer, player)) {
            goTo(new Choosing(this, currentPlayer));
        } else {
            goTo(new Waiting(this));
        }
    }

    @Override
    public void select(Enemy enemy) {
        if (getMap().canTargetAfterMove(currentPlayer, enemy)) {
            Optional<Coordinate> moveTarget = getMap().getMoveForTarget(currentPlayer, enemy.getCoordinate());
            if (moveTarget.isPresent()) {
                getMap().moveIfAble(currentPlayer, moveTarget.get());
                // TODO: enter battle prep
                getMarkings().removeEnemy(enemy);
                getMap().kill(enemy);
            }
            goTo(new Waiting(this));
        } else {
            goTo(new Waiting(this));
        }
    }

    @Override
    public void select(Terrain terrain) {
        boolean moved = getMap().moveIfAble(currentPlayer, terrain.getCoordinate());
        // we will consider clicking outside of movable area to be canceling
        if (moved) {
            goTo(new Choosing(this, currentPlayer));
        } else {
            goToPrevState();
        }
    }

    @Override
    ImmutableCollection<Action> getActions() {
        return ImmutableList.<Action>of(new Cancel(this));
    }
}
