package com.jingyuyao.tactical.model.state;

import com.google.common.base.Objects;
import com.jingyuyao.tactical.model.Enemy;
import com.jingyuyao.tactical.model.Player;
import com.jingyuyao.tactical.model.Terrain;

import java.util.Collection;

class Moving extends AbstractState {
    Moving(AbstractState prevState, Player selectedPlayer) {
        super(prevState);
        getStateData().selectedPlayer(selectedPlayer);
    }

    @Override
    public State select(Player player) {
        if (Objects.equal(getStateData().getSelectedPlayer(), player)) {
            return new Waiting(this);
        } else {
            return new Moving(this, player);
        }
    }

    @Override
    public State select(Enemy enemy) {
        Collection<Terrain> targetTerrains = getMap().getAllTargetTerrains(getStateData().getSelectedPlayer());
        Terrain enemyTerrain = getMap().getTerrain(enemy.getX(), enemy.getY());
        if (targetTerrains.contains(enemyTerrain)) {
            // TODO: Move character & enter battle prep
            return new Waiting(this);
        } else {
            return new Waiting(this);
        }
    }

    @Override
    public State select(Terrain terrain) {
        getMap().moveIfAble(getStateData().getSelectedPlayer(), terrain);
        if (getMap().hasAnyTarget(getStateData().getSelectedPlayer())) {
            return new Targeting(this);
        } else {
            // TODO: go to action state
            return new Waiting(this);
        }
    }
}
