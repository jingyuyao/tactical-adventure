package com.jingyuyao.tactical.model.state;

import com.google.common.base.Objects;
import com.jingyuyao.tactical.model.Enemy;
import com.jingyuyao.tactical.model.Player;
import com.jingyuyao.tactical.model.Terrain;

import java.util.Collection;

public class Moving extends AbstractState {
    Moving(AbstractState prevState, Player selectedPlayer) {
        super(prevState);
        getSelections().selectedPlayer(selectedPlayer);
    }

    @Override
    public SelectionState select(Player player) {
        if (Objects.equal(getSelections().getSelectedPlayer(), player)) {
            return new Waiting(this);
        } else {
            return new Moving(this, player);
        }
    }

    @Override
    public SelectionState select(Enemy enemy) {
        Collection<Terrain> targetTerrains = getMap().getAllTargetTerrains(getSelections().getSelectedPlayer());
        Terrain enemyTerrain = getMap().getTerrain(enemy.getX(), enemy.getY());
        if (targetTerrains.contains(enemyTerrain)) {
            // TODO: Move character & enter battle prep
            return new Waiting(this);
        } else {
            return new Waiting(this);
        }
    }

    @Override
    public SelectionState select(Terrain terrain) {
        getMap().moveIfAble(getSelections().getSelectedPlayer(), terrain);
        if (getMap().hasAnyTarget(getSelections().getSelectedPlayer())) {
            return new Targeting(this);
        } else {
            // TODO: go to action state
            return new Waiting(this);
        }
    }
}
