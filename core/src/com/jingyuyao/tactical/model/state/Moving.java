package com.jingyuyao.tactical.model.state;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.Coordinate;
import com.jingyuyao.tactical.model.object.Enemy;
import com.jingyuyao.tactical.model.object.Player;
import com.jingyuyao.tactical.model.object.Terrain;

class Moving extends AbstractPlayerState {
    private Coordinate previousCoordinate;

    Moving(AbstractState prevState, Player currentPlayer) {
        super(prevState, currentPlayer);
    }

    @Override
    public void enter() {
        super.enter();
        getMarkings().showMoveAndTargets(getTargetInfo());
    }

    @Override
    public void canceled() {
        if (previousCoordinate != null) {
            getCurrentPlayer().instantMoveTo(previousCoordinate);
        }
    }

    @Override
    public void select(Player player) {
        if (Objects.equal(getCurrentPlayer(), player)) {
            goTo(new Choosing(this));
        } else {
            goTo(new Moving(backToOrigin(), player));
        }
    }

    @Override
    public void select(final Enemy enemy) {
        if (getTargetInfo().canHitAfterMove(enemy)) {
            Coordinate moveCoordinate = getTargetInfo().moveForTarget(enemy.getCoordinate());
            ImmutableList<Coordinate> path = getTargetInfo().pathTo(moveCoordinate);
            if (path.isEmpty()) {
                throw new RuntimeException("Shouldn't be possible");
            }

            moveCurrentPlayer(moveCoordinate, path);
            // creates an intermediate choosing state so we can backtrack here if needed
            Choosing choosing = new Choosing(this);
            SelectingWeapon selectingWeapon = new SelectingWeapon(choosing, enemy);
            goTo(choosing);
            goTo(selectingWeapon);
        } else {
            back();
        }
    }

    @Override
    public void select(Terrain terrain) {
        if (getTargetInfo().canMoveTo(terrain.getCoordinate())) {
            ImmutableList<Coordinate> path = getTargetInfo().pathTo(terrain.getCoordinate());
            moveCurrentPlayer(terrain.getCoordinate(), path);
            goTo(new Choosing(this));
        } else {
            // we will consider clicking outside of movable area to be canceling
            back();
        }
    }

    @Override
    public ImmutableList<Action> getActions() {
        return ImmutableList.<Action>of(new Back(this));
    }

    private void moveCurrentPlayer(Coordinate destination, ImmutableList<Coordinate> path) {
        previousCoordinate = getCurrentPlayer().getCoordinate();
        getCurrentPlayer().moveTo(destination, path);
    }
}
