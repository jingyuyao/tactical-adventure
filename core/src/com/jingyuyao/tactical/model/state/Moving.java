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
    private ImmutableList<Coordinate> lastMovePath;

    Moving(AbstractState prevState, Player currentPlayer) {
        super(prevState);
        this.currentPlayer = currentPlayer;
        lastMovePath = ImmutableList.of();
    }

    @Override
    void enter() {
        getMarkings().markMoveAndTargets(currentPlayer);
    }

    @Override
    void canceled() {
        if (!lastMovePath.isEmpty()) {
            Coordinate previousCoordinate = lastMovePath.iterator().next();
            if (!previousCoordinate.equals(currentPlayer.getCoordinate())) {
                currentPlayer.moveTo(previousCoordinate, lastMovePath.reverse());
                lastMovePath = ImmutableList.of();
            }
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
            hardCancel();
        }
    }

    @Override
    public void select(final Enemy enemy) {
        if (getMap().canTargetAfterMove(currentPlayer, enemy)) {
            Optional<Coordinate> moveTarget = getMap().getMoveForTarget(currentPlayer, enemy.getCoordinate());
            if (moveTarget.isPresent()) {
                ImmutableList<Coordinate> path = getMap().getPathToTarget(currentPlayer, moveTarget.get());
                if (!path.isEmpty()) {
                    lastMovePath = path;
                    currentPlayer.moveTo(moveTarget.get(), path);
                    // creates an intermediate choosing state so we can backtrack here if needed
                    Choosing choosing = new Choosing(this, currentPlayer);
                    BattlePrepping battlePrepping = new BattlePrepping(choosing, currentPlayer, enemy);
                    goTo(choosing);
                    goTo(battlePrepping);
                }
            } else {
                hardCancel();
            }
        } else {
            hardCancel();
        }
    }

    @Override
    public void select(Terrain terrain) {
        ImmutableList<Coordinate> path = getMap().getPathToTarget(currentPlayer, terrain.getCoordinate());
        if (!path.isEmpty()) {
            lastMovePath = path;
            currentPlayer.moveTo(terrain.getCoordinate(), path);
            goTo(new Choosing(this, currentPlayer));
        } else {
            // we will consider clicking outside of movable area to be canceling
            hardCancel();
        }
    }

    @Override
    ImmutableCollection<Action> getActions() {
        return ImmutableList.<Action>of(new Back(this));
    }
}
