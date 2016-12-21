package com.jingyuyao.tactical.model.state;

import com.google.common.base.Objects;
import com.jingyuyao.tactical.model.Enemy;
import com.jingyuyao.tactical.model.Player;
import com.jingyuyao.tactical.model.Terrain;

import java.util.Collection;

class Moving extends AbstractState {
    private final Player selectedPlayer;

    Moving(AbstractState prevState, Player selectedPlayer) {
        super(prevState);
        this.selectedPlayer = selectedPlayer;
        getMarkings().markPlayer(selectedPlayer, false);
    }

    @Override
    public State select(Player player) {
        if (Objects.equal(selectedPlayer, player)) {
            return new Waiting(this);
        } else {
            return new Moving(this, player);
        }
    }

    @Override
    public State select(Enemy enemy) {
        Collection<Terrain> targetTerrains = getMap().getAllTargetTerrains(selectedPlayer);
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
        getMap().moveIfAble(selectedPlayer, terrain);
        if (getMap().hasAnyTarget(selectedPlayer)) {
            return new Targeting(this, selectedPlayer);
        } else {
            // TODO: go to action state
            return new Waiting(this);
        }
    }
}
