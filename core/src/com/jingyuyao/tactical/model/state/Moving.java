package com.jingyuyao.tactical.model.state;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.jingyuyao.tactical.model.Coordinate;
import com.jingyuyao.tactical.model.Enemy;
import com.jingyuyao.tactical.model.Player;
import com.jingyuyao.tactical.model.Terrain;

import java.util.ArrayList;
import java.util.Collection;

class Moving extends AbstractState {
    private final Player movingPlayer;

    Moving(AbstractState prevState, Player movingPlayer) {
        super(prevState);
        this.movingPlayer = movingPlayer;
        getMarkings().markPlayer(movingPlayer, false);
    }

    @Override
    public AbstractState select(Player player) {
        if (Objects.equal(movingPlayer, player)) {
            return new Waiting(this);
        } else {
            return new Moving(this, player);
        }
    }

    @Override
    public AbstractState select(Enemy enemy) {
        if (getMap().canTargetAfterMove(movingPlayer, enemy)) {
            Optional<Coordinate> moveTarget = getMap().getMoveForTarget(movingPlayer, enemy.getCoordinate());
            if (moveTarget.isPresent()) {
                getMap().moveIfAble(movingPlayer, moveTarget.get());
                // TODO: enter battle prep
                getMarkings().removeEnemy(enemy);
                getMap().kill(enemy);
            }
            return new Waiting(this);
        } else {
            return new Waiting(this);
        }
    }

    @Override
    public AbstractState select(Terrain terrain) {
        getMap().moveIfAble(movingPlayer, terrain.getCoordinate());
        if (getMap().hasAnyImmediateTarget(movingPlayer)) {
            return new Targeting(this, movingPlayer);
        } else {
            // TODO: go to action state
            return new Waiting(this);
        }
    }
}
