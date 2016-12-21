package com.jingyuyao.tactical.model.state;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.jingyuyao.tactical.model.Enemy;
import com.jingyuyao.tactical.model.Player;
import com.jingyuyao.tactical.model.Terrain;

import java.util.Collection;

class Moving extends AbstractState {
    private final Player movingPlayer;

    Moving(AbstractState prevState, Player movingPlayer) {
        super(prevState);
        this.movingPlayer = movingPlayer;
        getMarkings().markPlayer(movingPlayer, false);
    }

    @Override
    public State select(Player player) {
        if (Objects.equal(movingPlayer, player)) {
            return new Waiting(this);
        } else {
            return new Moving(this, player);
        }
    }

    @Override
    public State select(Enemy enemy) {
        Collection<Terrain> targetTerrains = getMap().getAllTargetTerrains(movingPlayer);
        Terrain enemyTerrain = getMap().getTerrain(enemy.getX(), enemy.getY());
        if (targetTerrains.contains(enemyTerrain)) {
            // TODO: enter battle prep
            Optional<Terrain> moveTarget = getMap().getMoveTerrainForTarget(movingPlayer, enemyTerrain);
            if (moveTarget.isPresent()) {
                getMap().moveIfAble(movingPlayer, moveTarget.get());
                getMarkings().removeEnemy(enemy);
                getMap().kill(enemy);
            }
            return new Waiting(this);
        } else {
            return new Waiting(this);
        }
    }

    @Override
    public State select(Terrain terrain) {
        getMap().moveIfAble(movingPlayer, terrain);
        if (getMap().hasAnyTarget(movingPlayer)) {
            return new Targeting(this, movingPlayer);
        } else {
            // TODO: go to action state
            return new Waiting(this);
        }
    }
}
