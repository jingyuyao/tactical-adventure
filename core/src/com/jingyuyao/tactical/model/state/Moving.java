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
    private Coordinate previousCoordinate;

    Moving(AbstractState prevState, Player currentPlayer) {
        super(prevState);
        this.currentPlayer = currentPlayer;
    }

    @Override
    void enter() {
        getMarkings().markMoveAndTargets(currentPlayer);
    }

    @Override
    void canceled() {
        if (previousCoordinate != null) {
            currentPlayer.instantMoveTo(previousCoordinate);
        }
    }

    @Override
    void exit() {
        getMarkings().unMarkLastPlayer();
    }

    @Override
    public void select(Player player) {
        if (Objects.equal(currentPlayer, player)) {
            goTo(new Choosing(this, currentPlayer));
        } else {
            goTo(new Moving(backToWaiting(), player));
        }
    }

    @Override
    public void select(final Enemy enemy) {
        if (getMap().canTargetAfterMove(currentPlayer, enemy)) {
            Optional<Coordinate> moveTarget = getMap().getMoveForTarget(currentPlayer, enemy.getCoordinate());
            if (!moveTarget.isPresent()) {
                throw new RuntimeException("Shouldn't be possible");
            }

            ImmutableList<Coordinate> path = getMap().getPathToTarget(currentPlayer, moveTarget.get());
            if (path.isEmpty()) {
                throw new RuntimeException("Shouldn't be possible");
            }

            moveCurrentPlayer(moveTarget.get(), path);
            // creates an intermediate choosing state so we can backtrack here if needed
            Choosing choosing = new Choosing(this, currentPlayer);
            SelectingWeapon selectingWeapon = new SelectingWeapon(choosing, currentPlayer, enemy);
            goTo(choosing);
            goTo(selectingWeapon);
        } else {
            back();
        }
    }

    @Override
    public void select(Terrain terrain) {
        ImmutableList<Coordinate> path = getMap().getPathToTarget(currentPlayer, terrain.getCoordinate());
        if (!path.isEmpty()) {
            moveCurrentPlayer(terrain.getCoordinate(), path);
            goTo(new Choosing(this, currentPlayer));
        } else {
            // we will consider clicking outside of movable area to be canceling
            back();
        }
    }

    @Override
    ImmutableCollection<Action> getActions() {
        return ImmutableList.<Action>of(new Back(this));
    }

    private void moveCurrentPlayer(Coordinate destination, ImmutableList<Coordinate> path) {
        previousCoordinate = currentPlayer.getCoordinate();
        currentPlayer.moveTo(destination, path);
    }
}
